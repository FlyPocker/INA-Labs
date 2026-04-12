class Pentagon implements Figure{
    protected double side;
    public Pentagon(double side){
        this.side = side;
    }
    @Override
    public String getName(){
        return "Pentagon";
    }
    @Override
    public double getPerimeter(){
        return side*5;
    }
    @Override
    public double getArea(){
        return (Math.sqrt(25+(10*Math.sqrt(5)))*this.side*this.side)/4;
    }
}