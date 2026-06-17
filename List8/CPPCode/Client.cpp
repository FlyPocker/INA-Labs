#include <iostream>
#include <string>
#include <cstring>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>

const int PORT = 1234;

std::string readLine(int sock) {
    std::string line = "";
    char ch;
    while (recv(sock, &ch, 1, 0) > 0) {
        if (ch == '\n') break;
        if (ch != '\r') line += ch;
    }
    return line;
}

int main() {
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock == -1) {
        std::cerr << "Blad: Nie udalo sie utworzyc gniazda.\n";
        return 1;
    }

    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    
    if (inet_pton(AF_INET, "127.0.0.1", &serverAddr.sin_addr) <= 0) {
        std::cerr << "Blad: Niepoprawny adres IP.\n";
        return 1;
    }

    if (connect(sock, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) < 0) {
        std::cerr << "BLAD: Brak polaczenia. Upewnij sie, ze serwer Java jest uruchomiony!\n";
        close(sock);
        return 1;
    }

    std::cout << "Polaczono pomyslnie z serwerem (127.0.0.1:" << PORT << ")\n";
    std::cout << "Dostepne komendy:\n";
    std::cout << "  ADD [integer/double/string/point] [klucz]\n";
    std::cout << "  GET [integer/double/string/point] [klucz]\n";
    std::cout << "  DELETE [integer/double/string/point] [klucz]\n";
    std::cout << "  SHOW [integer/double/string/point]\n";
    std::cout << "Wpisz 'exit' aby zakonczyc.\n\n";

    std::string command;
    while (true) {
        std::cout << "Klient> ";
        std::getline(std::cin, command);

        if (command == "exit" || command == "EXIT") break;
        if (command.empty()) continue;

        std::string fullCmd = command + "\n";
        send(sock, fullCmd.c_str(), fullCmd.length(), 0);

        std::string response = readLine(sock);
        std::cout << "Serwer: " << response << "\n\n";
    }

    close(sock);
    std::cout << "Rozlaczono.\n";
    return 0;
}