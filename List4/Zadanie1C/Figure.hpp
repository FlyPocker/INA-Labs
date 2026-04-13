#pragma once
#include <string>

class Figure {
public:
    virtual std::string getName() const = 0;
    virtual double getPerimeter() const = 0;
    virtual double getArea() const = 0;
    
    virtual ~Figure() = default; 
};