class Diamond extends Quadrangle{
    public Diamond(double side, double angle){
        super(side, side, side, side, angle);
    }
    @Override
    public String getName(){
        return "Diamond";
    }
    @Override
    public double getArea(){
        return this.sides[0]*this.sides[0]*Math.sin(Math.toRadians(this.angle));
    }
}