#ifndef CLIENT_HANDLER_HPP
#define CLIENT_HANDLER_HPP

#include "BT.hpp"
#include "Point2D.hpp"
#include <string>

class ClientHandler {
private:
    int clientSocket;
    BT<int, std::string>& intTree;
    BT<double, std::string>& doubleTree;
    BT<std::string, std::string>& stringTree;
    BT<Point2D, std::string>& pointTree;

    std::string readLine();
    void sendLine(const std::string& line);
    std::string handleCommand(const std::string& input);
    void replaceAll(std::string& str, const std::string& from, const std::string& to);

public:
    ClientHandler(int socket, 
                  BT<int, std::string>& it, 
                  BT<double, std::string>& dt, 
                  BT<std::string, std::string>& st,
                  BT<Point2D, std::string>& pt);
                  
    void run();
};

#endif // CLIENT_HANDLER_HPP