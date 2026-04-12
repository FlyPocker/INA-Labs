class Hexagon implements Figure{
    protected double side;
    public Hexagon(double side){
        this.side = side;
    }
    @Override
    public String getName(){
        return "Hexagon";
    }
    @Override
    public double getPerimeter(){
        return side*6;
    }
    @Override
    public double getArea(){
        return (3*Math.sqrt(3)*side*side)/2;
    }
}