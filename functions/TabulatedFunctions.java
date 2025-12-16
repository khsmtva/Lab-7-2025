package functions;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {
    
    // Приватный конструктор - нельзя создать объект класса
    private TabulatedFunctions() {
        throw new UnsupportedOperationException("Нельзя создать объект класса TabulatedFunctions");
    }
    
    // Фабрика по умолчанию
    private static TabulatedFunctionFactory factory = 
        new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    
    // Метод для установки фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }
    
    //МЕТОДЫ СОЗДАНИЯ ЧЕРЕЗ ФАБРИКУ 
    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }
    
    // МЕТОДЫ СОЗДАНИЯ ЧЕРЕЗ РЕФЛЕКСИЮ 
    
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                             double leftX, double rightX, int pointsCount) {
        try {
            // Находим конструктор с параметрами (double, double, int)
            Constructor<? extends TabulatedFunction> constructor = 
                functionClass.getConstructor(double.class, double.class, int.class);
            
            // Создаем объект через конструктор
            return constructor.newInstance(leftX, rightX, pointsCount);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + 
                                               " не имеет конструктора (double, double, int)", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Не удалось создать экземпляр класса " + 
                                               functionClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Конструктор класса " + functionClass.getName() + 
                                               " недоступен", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе класса " + 
                                               functionClass.getName(), e);
        }
    }
    
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                             double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor = 
                functionClass.getConstructor(double.class, double.class, double[].class);
            
            return constructor.newInstance(leftX, rightX, values);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + 
                                               " не имеет конструктора (double, double, double[])", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Не удалось создать экземпляр класса " + 
                                               functionClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Конструктор класса " + functionClass.getName() + 
                                               " недоступен", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе класса " + 
                                               functionClass.getName(), e);
        }
    }
    
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                             FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> constructor = 
                functionClass.getConstructor(FunctionPoint[].class);
            
            return constructor.newInstance((Object) points);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + 
                                               " не имеет конструктора (FunctionPoint[])", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Не удалось создать экземпляр класса " + 
                                               functionClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Конструктор класса " + functionClass.getName() + 
                                               " недоступен", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе класса " + 
                                               functionClass.getName(), e);
        }
    }
    
    // МЕТОДЫ ТАБУЛИРОВАНИЯ
    
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        // Создаем массив точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем фабрику вместо прямого создания
        return createTabulatedFunction(points);
    }
    
    // Табулирование с указанием класса через рефлексию
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass,
                                             Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        // Создаем массив точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем рефлексию для создания объекта нужного класса
        return createTabulatedFunction(functionClass, points);
    }
    
    // МЕТОДЫ ВВОДА/ВЫВОДА
    
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            dos.writeDouble(point.getX());
            dos.writeDouble(point.getY());
        }
        
        dos.flush();
    }
    
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        
        return createTabulatedFunction(points);
    }
    
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        
        return createTabulatedFunction(points);
    }
}