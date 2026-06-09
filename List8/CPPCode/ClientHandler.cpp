#include "ClientHandler.hpp"
#include <iostream>
#include <sstream>
#include <vector>
#include <sys/socket.h>
#include <unistd.h>

ClientHandler::ClientHandler(int socket, BT<int, std::string>& it, BT<double, std::string>& dt, BT<std::string, std::string>& st)
    : clientSocket(socket), intTree(it), doubleTree(dt), stringTree(st) {}

void ClientHandler::run() {
    std::string inputLine;
    // Czytamy linie od klienta tak długo, jak połączenie jest otwarte
    while (!(inputLine = readLine()).empty()) {
        std::string response = handleCommand(inputLine);
        sendLine(response);
    }
    // Zamknięcie gniazda po rozłączeniu klienta
    close(clientSocket);
    std::cout << "Klient rozłaczony.\n";
}

std::string ClientHandler::readLine() {
    std::string line = "";
    char ch;
    // Czytamy po jednym bajcie (znaku), aż trafimy na enter \n
    while (recv(clientSocket, &ch, 1, 0) > 0) {
        if (ch == '\n') break;
        if (ch != '\r') line += ch;
    }
    return line;
}

void ClientHandler::sendLine(const std::string& line) {
    std::string fullLine = line + "\n";
    send(clientSocket, fullLine.c_str(), fullLine.length(), 0);
}

void ClientHandler::replaceAll(std::string& str, const std::string& from, const std::string& to) {
    size_t start_pos = 0;
    while ((start_pos = str.find(from, start_pos)) != std::string::npos) {
        str.replace(start_pos, from.length(), to);
        start_pos += to.length();
    }
}

std::string ClientHandler::handleCommand(const std::string& input) {
    if (input.empty()) return "ERROR: Pusta komenda";

    std::stringstream ss(input);
    std::string command, type, keyStr, value;

    ss >> command;
    // Zamiana komendy na wielkie litery dla elastyczności
    for (char &c : command) c = toupper(c);

    try {
        if (command == "ADD") {
            ss >> type >> keyStr;
            // Pobieramy resztę strumienia jako wartość (może zawierać spacje)
            std::getline(ss >> std::ws, value); 
            if (type.empty() || keyStr.empty() || value.empty()) return "ERROR: Składnia: ADD [typ] [klucz] [wartosc]";
            
            for (char &char_type : type) char_type = toupper(char_type);

            if (type == "INTEGER") {
                intTree.insert(std::stoi(keyStr), value);
            } else if (type == "DOUBLE") {
                doubleTree.insert(std::stod(keyStr), value);
            } else {
                stringTree.insert(keyStr, value);
            }
            return "OK: Dodano pomyslnie na drzewie " + type;
        } 
        else if (command == "GET") {
            ss >> type >> keyStr;
            if (type.empty() || keyStr.empty()) return "ERROR: Składnia: GET [typ] [klucz]";
            
            for (char &char_type : type) char_type = toupper(char_type);
            std::string resVal;
            bool found = false;

            if (type == "INTEGER") {
                found = intTree.search(std::stoi(keyStr), resVal);
            } else if (type == "DOUBLE") {
                found = doubleTree.search(std::stod(keyStr), resVal);
            } else {
                found = stringTree.search(keyStr, resVal);
            }
            return found ? "VALUE: " + resVal : "NOT_FOUND: Brak klucza na drzewie " + type;
        } 
        else if (command == "DELETE") {
            ss >> type >> keyStr;
            if (type.empty() || keyStr.empty()) return "ERROR: Składnia: DELETE [typ] [klucz]";
            
            for (char &char_type : type) char_type = toupper(char_type);

            if (type == "INTEGER") {
                intTree.remove(std::stoi(keyStr));
            } else if (type == "DOUBLE") {
                doubleTree.remove(std::stod(keyStr));
            } else {
                stringTree.remove(keyStr);
            }
            return "OK: Usunieto z drzewa " + type;
        } 
        else if (command == "SHOW") {
            ss >> type;
            if (type.empty()) return "ERROR: Składnia: SHOW [typ]";
            
            for (char &char_type : type) char_type = toupper(char_type);
            std::string structure;

            if (type == "INTEGER") {
                structure = intTree.toBottomUpPyramidString();
            } else if (type == "DOUBLE") {
                structure = doubleTree.toBottomUpPyramidString();
            } else {
                structure = stringTree.toBottomUpPyramidString();
            }

            // Podmieniamy entery na znacznik internetowy <BR> przed wysłaniem
            replaceAll(structure, "\n", "<BR>");
            return "TREE: <BR>" + structure;
        }

        return "ERROR: Nieznana komenda. Dostepne: ADD, GET, DELETE, SHOW";
    } 
    // Przechwytywanie wyjątków konwersji typów (np. tekst zamiast liczby)
     catch (const std::invalid_argument& e) {
        return "ERROR: Blad formatu klucza! Upewnij sie, ze pasuje do wybranego typu.";
    } catch (const std::exception& e) {
        return "ERROR: Blad operacji: " + std::string(e.what());
    }
}