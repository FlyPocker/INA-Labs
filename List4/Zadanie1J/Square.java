class Square extends Quadrangle{
    public Square(double side){
        super(side,side,side,side,90);
    }
    @Override
    public String getName(){
        return "Square";
    }
    @Override
    public double getArea(){
        return this.sides[0]*this.sides[0];
    }
}