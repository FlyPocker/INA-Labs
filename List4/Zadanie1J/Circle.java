class Circle implements Figure{
    protected double radius;
    public Circle(double radius){
        this.radius = radius;
    }
    @Override
    public String getName(){
        return "Circle";
    }
    @Override
    public double getPerimeter(){
        return 2*Math.PI*this.radius;
    }
    @Override
    public  double getArea(){
        return Math.PI*this.radius*this.radius;
    }
}