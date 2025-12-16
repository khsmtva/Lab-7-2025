package functions;

public class FunctionPoint implements java.io.Serializable {
    private double x;
    private double y;
    
    // Конструктор с параметрами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    // Конструктор копирования
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    
    // Конструктор по умолчанию
    public FunctionPoint() {
        this(0, 0);
    }
    
    // Геттеры
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    // Сеттеры
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FunctionPoint that = (FunctionPoint) o;
        return Math.abs(x - that.x) < 1e-10 && 
               Math.abs(y - that.y) < 1e-10;
    }

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));
        
        return xHash ^ yHash;
    }

     @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new FunctionPoint(this.x, this.y);
        }
    }
}

