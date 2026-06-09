#include "BT.hpp"
#include "ClientHandler.hpp"
#include <iostream>
#include <thread>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

const int PORT = 1234;

int main() {
    // 1. Trzy współdzielone drzewa dla różnych typów kluczy
    BT<int, std::string> intTree;
    BT<double, std::string> doubleTree;
    BT<std::string, std::string> stringTree;

    // 2. Tworzenie gniazda serwera (IPv4, TCP)
    int serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1) {
        std::cerr << "Blad podczas tworzenia gniazda serwera.\n";
        return 1;
    }

    // Pozwól na ponowne użycie portu (zapobiega błędowi "Address already in use")
    int opt = 1;
    setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    // 3. Podpięcie pod adres i port
    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY; // Nasłuchuj na wszystkich interfejsach
    serverAddr.sin_port = htons(PORT);

    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) < 0) {
        std::cerr << "Blad podczas bindowania portu " << PORT << ".\n";
        close(serverSocket);
        return 1;
    }

    // 4. Rozpoczęcie nasłuchiwania (maksymalna kolejka: 10 oczekujących połączeń)
    if (listen(serverSocket, 10) < 0) {
        std::cerr << "Blad podczas uruchamiania nasluchiwania.\n";
        close(serverSocket);
        return 1;
    }

    std::cout << "Serwer C++ uruchomiony. Oczekiwanie na polaczenia na porcie " << PORT << "...\n";

    // 5. Główna pętla akceptująca klientów
    while (true) {
        sockaddr_in clientAddr{};
        socklen_t clientLen = sizeof(clientAddr);
        
        // Akceptacja połączenia (blokuje wątek do momentu połączenia klienta)
        int clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientLen);
        if (clientSocket < 0) {
            std::cerr << "Blad podczas akceptowania klienta.\n";
            continue;
        }

        std::cout << "Nowy klient polaczony przez socket: " << clientSocket << "\n";

        // Tworzymy obiekt handlera w pamięci dynamicznej (heap)
        ClientHandler* handler = new ClientHandler(clientSocket, intTree, doubleTree, stringTree);

        // Uruchamiamy wątek i natychmiast go odpinamy (.detach()), 
        // dzięki czemu zasoby zwolnią się same po zakończeniu funkcji run()
        std::thread([handler]() {
            handler->run();
            delete handler; // Sprzątamy pamięć po rozłączeniu klienta
        }).detach();
    }

    close(serverSocket);
    return 0;
}