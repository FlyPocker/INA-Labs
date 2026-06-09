#include <iostream>
#include <string>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>

const int PORT = 1234;

// Funkcja pomocnicza do zamiany znaczników sieciowych <BR> na znaki nowej linii \n
void replaceAll(std::string& str, const std::string& from, const std::string& to) {
    size_t start_pos = 0;
    while ((start_pos = str.find(from, start_pos)) != std::string::npos) {
        str.replace(start_pos, from.length(), to);
        start_pos += to.length();
    }
}

// Funkcja do odbierania pełnej linii tekstu (do znaku \n) z socketu
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
        std::cerr << "Nie udalo sie utworzyc gniazda.\n";
        return 1;
    }

    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    // Łączymy się z localhost (127.0.0.1)
    if (inet_pton(AF_INET, "127.0.0.1", &serverAddr.sin_addr) <= 0) {
        std::cerr << "Niepoprawny adres IP.\n";
        return 1;
    }

    if (connect(sock, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) < 0) {
        std::cerr << "BLAD: Brak polaczenia. Uruchom najpierw serwer!\n";
        close(sock);
        return 1;
    }

    std::cout << "Polaczono pomyslnie z serwerem (127.0.0.1:" << PORT << ")\n";
    std::cout << "Dostepne komendy:\n";
    std::cout << "  ADD [integer/double/string] [klucz] [wartosc]\n";
    std::cout << "  GET [integer/double/string] [klucz]\n";
    std::cout << "  DELETE [integer/double/string] [klucz]\n";
    std::cout << "  SHOW [integer/double/string]\n";
    std::cout << "Wpisz 'exit' aby zakonczyc.\n\n";

    std::string command;
    while (true) {
        std::cout << "Klient> ";
        std::getline(std::cin, command);

        if (command == "exit" || command == "EXIT") {
            break;
        }
        if (command.empty()) {
            continue;
        }

        // Wysyłamy komendę do serwera (pamiętając o dodaniu \n na końcu!)
        std::string fullCmd = command + "\n";
        send(sock, fullCmd.c_str(), fullCmd.length(), 0);

        // Odbieramy odpowiedź z serwera
        std::string response = readLine(sock);
        
        // Formatujemy odpowiedź sieciową (zamiana <BR> na entery)
        replaceAll(response, "<BR>", "\n");

        std::cout << "Serwer: " << response << "\n\n";
    }

    close(sock);
    std::cout << "Rozlaczono.\n";
    return 0;
}