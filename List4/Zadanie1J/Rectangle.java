class Rectangle extends Quadrangle{
    public Rectangle(double side1, double side2){
        super(side1, side1, side2, side2, 90);
    }
    @Override
    public String getName(){
        return "Rectangle";
    }
    @Override
    public double getArea(){
        return this.sides[0]*this.sides[2];
    }
}