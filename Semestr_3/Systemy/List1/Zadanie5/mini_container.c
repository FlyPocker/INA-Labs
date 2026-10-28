#define _GNU_SOURCE
#include <sched.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/mount.h>
#include <unistd.h>

#define STACK_SIZE (1024 * 1024) // 1 MB stosu dla procesu potomnego
static char child_stack[STACK_SIZE];

struct container_config {
    char *rootfs;
};

int child_main(void *arg) {
    struct container_config *config = (struct container_config *)arg;

    // 1. Zmiana nazwy hosta
    if (sethostname("kontener-demo", 13) != 0) {
        perror("sethostname");
        return 1;
    }

    // 2. Izolacja punktów montowania (zabezpieczenie przed propagacją na hosta)
    mount(NULL, "/", NULL, MS_REC | MS_PRIVATE, NULL);

    // 3. Zmiana rootfs
    if (chroot(config->rootfs) != 0 || chdir("/") != 0) {
        perror("chroot/chdir");
        return 1;
    }

    // 4. Montowanie wirtualnego systemu plików /proc dla nowych PID-ów
    mount("proc", "/proc", "proc", 0, NULL);

    // 5. Uruchomienie interaktywnej powłoki
    char *args[] = { "/bin/sh", NULL };
    execvp(args[0], args);
    perror("execvp");
    return 1;
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Użycie: sudo %s <sciezka_do_rootfs>\n", argv[0]);
        return 1;
    }

    struct container_config config = { .rootfs = argv[1] };

    // Flagi izolacji Linuksa (dodano CLONE_NEWNET dla izolacji sieci)
    int flags = CLONE_NEWPID | CLONE_NEWUTS | CLONE_NEWNS | CLONE_NEWNET | SIGCHLD;

    // Utworzenie nowego procesu w osobnej przestrzeni nazw
    pid_t child_pid = clone(child_main, child_stack + STACK_SIZE, flags, &config);
    if (child_pid == -1) {
        perror("clone");
        return 1;
    }
    
    printf("[HOST] Utworzono kontener. PID dziecka: %d\n", child_pid);
    char cmd[256];

    // --- ZADANIE 2: Ograniczenia zasobów (cgroups) ---
    // Tworzymy nową grupę i ustawiamy twardy limit 50MB RAM. Wyłączamy swap, by wymusić OOM Kill.
    system("mkdir -p /sys/fs/cgroup/mini_kontener");
    system("echo 50M > /sys/fs/cgroup/mini_kontener/memory.max");
    system("echo 0 > /sys/fs/cgroup/mini_kontener/memory.swap.max"); 
    
    // Przypisujemy proces naszego kontenera do utworzonej grupy z limitami
    sprintf(cmd, "echo %d > /sys/fs/cgroup/mini_kontener/cgroup.procs", child_pid);
    system(cmd);

    // --- ZADANIE 1: Izolacja sieciowa (veth) ---
    // Tworzymy "wirtualny kabel" z dwiema końcówkami (veth0 i veth1)
    system("ip link add veth0 type veth peer name veth1");
    
    // Przekazujemy końcówkę veth1 do odizolowanej przestrzeni sieciowej kontenera
    sprintf(cmd, "ip link set veth1 netns %d", child_pid);
    system(cmd);
    
    // Podnosimy interfejs veth0 po stronie hosta i nadajemy mu adres IP
    system("ip link set veth0 up");
    system("ip addr add 10.0.0.1/24 dev veth0");

    // Czekanie na zakończenie kontenera (aż użytkownik wpisze 'exit')
    waitpid(child_pid, NULL, 0);

    // Sprzątanie po wyłączeniu kontenera
    printf("[HOST] Kontener wyłączony. Sprzątanie zasobów...\n");
    system("ip link del veth0 2>/dev/null");      // Usunięcie interfejsu sieciowego
    system("rmdir /sys/fs/cgroup/mini_kontener"); // Usunięcie grupy limitów

    return 0;
}