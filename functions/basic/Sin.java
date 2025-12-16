package functions.basic;

public class Sin implements functions.Function {
    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    
    @Override
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}