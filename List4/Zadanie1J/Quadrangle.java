abstract class Quadrangle implements Figure{
    protected double[] sides = new double[4];
    protected double angle;
    
    public Quadrangle(double side1, double side2, double side3, double side4, double angle){
        this.sides[0]=side1;
        this.sides[1]=side2;
        this.sides[2]=side3;
        this.sides[3]=side4;
        this.angle = angle;
    }
    
    @Override
    public double getPerimeter(){
        return sides[0]+sides[1]+sides[2]+sides[3];
    }
    
}