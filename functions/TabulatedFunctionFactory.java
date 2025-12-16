package functions;

public interface TabulatedFunctionFactory {
    // Создает табулированную функцию по границам и количеству точек
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount);
    
    // Создает табулированную функцию по границам и массиву значений
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values);
    
    // Создает табулированную функцию по массиву точек
    TabulatedFunction createTabulatedFunction(FunctionPoint[] points);
}
