import functions.*;
import functions.basic.*;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА №7 ===");
        
        System.out.println("\n--- ЗАДАНИЕ 1:");
        testIterator();
        
        System.out.println("\n--- ЗАДАНИЕ 2:");
        testFactoryMethod();
        
        System.out.println("\n--- ЗАДАНИЕ 3:");
        testReflection();
        
        System.out.println("\n=== Все задания выполнены ===");
    }
    
    public static void testIterator() {
        System.out.println("\n1. Тест ArrayTabulatedFunction:");
        FunctionPoint[] points = {
            new FunctionPoint(0, 0),
            new FunctionPoint(1, 1),
            new FunctionPoint(2, 4)
        };
        
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);
        System.out.println("   Итерация через for-each:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println("     " + p);
        }
        
        System.out.println("\n2. Тест LinkedListTabulatedFunction:");
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(points);
        System.out.println("   Итерация через for-each:");
        for (FunctionPoint p : listFunc) {
            System.out.println("     " + p);
        }
        
        System.out.println("\n3. Тест итератора вручную:");
        Iterator<FunctionPoint> it = arrayFunc.iterator();
        while (it.hasNext()) {
            System.out.println("     " + it.next());
        }
        
        System.out.println("\n4. Тест исключений:");
        try {
            Iterator<FunctionPoint> iterator = arrayFunc.iterator();
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("   Корректно: UnsupportedOperationException - " + e.getMessage());
        }
        
        try {
            Iterator<FunctionPoint> iterator = arrayFunc.iterator();
            while (iterator.hasNext()) iterator.next();
            iterator.next();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("   Корректно: NoSuchElementException - " + e.getMessage());
        }
    }
    
    public static void testFactoryMethod() {
        System.out.println("\n1. Тест фабрики по умолчанию:");
        Function cos = new Cos();
        TabulatedFunction tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 5);
        System.out.println("   Тип: " + tf.getClass().getSimpleName());
        
        System.out.println("\n2. Смена фабрики на LinkedListTabulatedFunction:");
        TabulatedFunctions.setTabulatedFunctionFactory(
            new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory()
        );
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 5);
        System.out.println("   Тип: " + tf.getClass().getSimpleName());
        
        System.out.println("\n3. Возврат к ArrayTabulatedFunction:");
        TabulatedFunctions.setTabulatedFunctionFactory(
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory()
        );
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 5);
        System.out.println("   Тип: " + tf.getClass().getSimpleName());
    }
    
    public static void testReflection() {
        System.out.println("\n1. Создание через рефлексию:");
        
        TabulatedFunction f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("   ArrayTabulatedFunction(0, 10, 3):");
        System.out.println("     Тип: " + f.getClass().getSimpleName());
        System.out.println("     Данные: " + f);
        
        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
        System.out.println("\n   ArrayTabulatedFunction(0, 10, [0,5,10]):");
        System.out.println("     Тип: " + f.getClass().getSimpleName());
        
        f = TabulatedFunctions.createTabulatedFunction(
            LinkedListTabulatedFunction.class, 
            new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(5, 25),
                new FunctionPoint(10, 100)
            }
        );
        System.out.println("\n   LinkedListTabulatedFunction(points):");
        System.out.println("     Тип: " + f.getClass().getSimpleName());
        
        System.out.println("\n2. Tabulate с рефлексией:");
        Sin sin = new Sin();
        f = TabulatedFunctions.tabulate(
            LinkedListTabulatedFunction.class, sin, 0, Math.PI, 5);
        System.out.println("   Тип: " + f.getClass().getSimpleName());
        System.out.println("   Точек: " + f.getPointsCount());
        
        System.out.println("\n3. Обработка ошибок:");
        try {
            f = TabulatedFunctions.createTabulatedFunction(
                TabulatedFunction.class, 0, 10, 3);
        } catch (IllegalArgumentException e) {
            System.out.println("   Корректно: " + e.getMessage());
        }
        
        System.out.println("\n Методы ввода/вывода с рефлексией:");
        try {
            TabulatedFunction original = new ArrayTabulatedFunction(0, 5, 3);
            
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(original, baos);
            
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(baos.toByteArray());
            TabulatedFunction readFunc = TabulatedFunctions.inputTabulatedFunction(
                LinkedListTabulatedFunction.class, bais);
            
            System.out.println("   Чтение в LinkedListTabulatedFunction: " + 
                (original.equals(readFunc) ? "успешно" : "ошибка"));
        } catch (Exception e) {
            System.out.println("   Ошибка: " + e.getMessage());
        }
        
        // Тест перегруженных методов записи с рефлексией
        System.out.println("\n Тест перегруженных методов записи с рефлексией:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 3);
            
            // Тест outputTabulatedFunction с рефлексией
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(ArrayTabulatedFunction.class, func, baos);
            System.out.println("   outputTabulatedFunction с Class: успешно");
            
            // Тест writeTabulatedFunction с рефлексией  
            java.io.StringWriter sw = new java.io.StringWriter();
            TabulatedFunctions.writeTabulatedFunction(ArrayTabulatedFunction.class, func, sw);
            System.out.println("   writeTabulatedFunction с Class: успешно");
            
            // Тест ошибки при несоответствии типа
            try {
                TabulatedFunctions.outputTabulatedFunction(LinkedListTabulatedFunction.class, func, baos);
            } catch (IllegalArgumentException e) {
                System.out.println("   Ошибка при несоответствии типа: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("   Ошибка: " + e.getMessage());
        }
    }
}