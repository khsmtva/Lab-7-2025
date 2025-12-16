package functions;

import functions.meta.*;

public final class Functions {
    
    // Приватный конструктор - нельзя создать объект класса
    private Functions() {
        throw new UnsupportedOperationException("Нельзя создать объект класса Functions");
    }
    
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
    public static double integrate(Function function, double a, double b, double step) {
        // Проверка корректности параметров
        if (a < function.getLeftDomainBorder()) {
            throw new IllegalArgumentException(
                "Левая граница интегрирования " + a + 
                " выходит за левую границу области определения " + function.getLeftDomainBorder()
            );
        }
        if (b > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                "Правая граница интегрирования " + b + 
                " выходит за правую границу области определения " + function.getRightDomainBorder()
            );
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным: " + step);
        }
        if (a >= b) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + a + " >= " + b);
        }
        
        double integral = 0.0;
        double x = a;
        
        // Метод трапеций
        while (x < b) {
            double xNext = Math.min(x + step, b); // Последний шаг может быть меньше
            double y1 = function.getFunctionValue(x);
            double y2 = function.getFunctionValue(xNext);
            
            // Площадь трапеции: (основание1 + основание2) * высота / 2
            integral += (y1 + y2) * (xNext - x) / 2.0;
            x = xNext;
        }
        
        return integral;
    }

}
