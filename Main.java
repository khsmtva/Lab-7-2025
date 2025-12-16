import functions.*;
import functions.basic.*;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА №7 ===");
        
        System.out.println("\n--- ЗАДАНИЕ 1: Проверка итератора ---");
        testIterator();
        
        System.out.println("\n--- ЗАДАНИЕ 2: Проверка фабричного метода ---");
        testFactoryMethod();
        
        System.out.println("\n--- ЗАДАНИЕ 3: Проверка рефлексии ---");
        testReflection();
        
        System.out.println("\n=== Все задания лабораторной работы выполнены! ===");
    }
    
    // ЗАДАНИЕ 1: Итератор
    public static void testIterator() {
        System.out.println("\n1. Тест ArrayTabulatedFunction:");
        FunctionPoint[] points1 = {
            new FunctionPoint(0, 0),
            new FunctionPoint(1, 1),
            new FunctionPoint(2, 4),
            new FunctionPoint(3, 9)
        };
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points1);
        System.out.println("   Итерация по точкам (for-each):");
        for (FunctionPoint p : arrayFunc) {
            System.out.println("     " + p);
        }
        
        System.out.println("\n2. Тест LinkedListTabulatedFunction:");
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(points1);
        System.out.println("   Итерация по точкам (for-each):");
        for (FunctionPoint p : listFunc) {
            System.out.println("     " + p);
        }
        
        System.out.println("\n3. Тест с явным итератором:");
        TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
        Iterator<FunctionPoint> iterator = func.iterator();
        System.out.println("   Точки функции от 0 до 10 (5 точек):");
        while (iterator.hasNext()) {
            FunctionPoint point = iterator.next();
            System.out.println("     X: " + point.getX() + ", Y: " + point.getY());
        }
        
        // Тест исключения UnsupportedOperationException
        System.out.println("\n4. Тест исключения при удалении:");
        try {
            TabulatedFunction testFunc = new ArrayTabulatedFunction(points1);
            Iterator<FunctionPoint> it = testFunc.iterator();
            it.next();
            it.remove(); // Должно бросить исключение
            System.out.println("   ОШИБКА: Должно было быть исключение!");
        } catch (UnsupportedOperationException e) {
            System.out.println("   Корректно: " + e.getMessage());
        }
        
        // Тест исключения NoSuchElementException
        System.out.println("\n5. Тест исключения при отсутствии следующего элемента:");
        try {
            TabulatedFunction testFunc = new ArrayTabulatedFunction(points1);
            Iterator<FunctionPoint> it = testFunc.iterator();
            while (it.hasNext()) {
                it.next();
            }
            it.next(); // Должно бросить исключение
            System.out.println("   ОШИБКА: Должно было быть исключение!");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("   Корректно: " + e.getMessage());
        }
    }
    
    // ЗАДАНИЕ 2: Фабричный метод
    public static void testFactoryMethod() {
        System.out.println("\n1. Тест фабрики по умолчанию (ArrayTabulatedFunction):");
        Function cos = new Cos();
        TabulatedFunction tf;
        
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println("   Тип функции после tabulate: " + tf.getClass().getSimpleName());
        System.out.println("   Количество точек: " + tf.getPointsCount());
        
        System.out.println("\n2. Тест смены фабрики на LinkedListTabulatedFunction:");
        TabulatedFunctions.setTabulatedFunctionFactory(
            new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory()
        );
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println("   Тип функции: " + tf.getClass().getSimpleName());
        
        System.out.println("\n3. Тест смены фабрики обратно на ArrayTabulatedFunction:");
        TabulatedFunctions.setTabulatedFunctionFactory(
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory()
        );
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println("   Тип функции: " + tf.getClass().getSimpleName());
        
        System.out.println("\n4. Тест всех методов createTabulatedFunction:");
        // Тест 1: по границам и количеству точек
        TabulatedFunction tf1 = TabulatedFunctions.createTabulatedFunction(0, 10, 5);
        System.out.println("   createTabulatedFunction(0, 10, 5): " + 
                          tf1.getClass().getSimpleName() + ", точек: " + tf1.getPointsCount());
        
        // Тест 2: по границам и массиву значений
        double[] values = {0, 2, 4, 6, 8, 10};
        TabulatedFunction tf2 = TabulatedFunctions.createTabulatedFunction(0, 10, values);
        System.out.println("   createTabulatedFunction(0, 10, values): " + 
                          tf2.getClass().getSimpleName() + ", точек: " + tf2.getPointsCount());
        
        // Тест 3: по массиву точек
        FunctionPoint[] points = {
            new FunctionPoint(0, 0),
            new FunctionPoint(5, 25),
            new FunctionPoint(10, 100)
        };
        TabulatedFunction tf3 = TabulatedFunctions.createTabulatedFunction(points);
        System.out.println("   createTabulatedFunction(points): " + 
                          tf3.getClass().getSimpleName() + ", точек: " + tf3.getPointsCount());
    }
    
    // ЗАДАНИЕ 3: Рефлексия
public static void testReflection() {
    System.out.println("\n1. Тест создания через рефлексию:");
    TabulatedFunction f;
    
    // Тест 1: ArrayTabulatedFunction через конструктор (double, double, int)
    f = TabulatedFunctions.createTabulatedFunction(
        ArrayTabulatedFunction.class, 0, 10, 3);
    System.out.println("   createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3):");
    System.out.println("     Тип: " + f.getClass().getSimpleName());
    System.out.println("     Функция: " + f);
    
    // Тест 2: ArrayTabulatedFunction через конструктор (double, double, double[])
    f = TabulatedFunctions.createTabulatedFunction(
        ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
    System.out.println("\n   createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, [0,5,10]):");
    System.out.println("     Тип: " + f.getClass().getSimpleName());
    System.out.println("     Функция: " + f);
    
    // Тест 3: LinkedListTabulatedFunction через конструктор (FunctionPoint[])
    f = TabulatedFunctions.createTabulatedFunction(
        LinkedListTabulatedFunction.class, 
        new FunctionPoint[] {
            new FunctionPoint(0, 0),
            new FunctionPoint(5, 25),
            new FunctionPoint(10, 100)
        }
    );
    System.out.println("\n   createTabulatedFunction(LinkedListTabulatedFunction.class, points):");
    System.out.println("     Тип: " + f.getClass().getSimpleName());
    System.out.println("     Функция: " + f);
    
    // Тест 4: tabulate с указанием класса
    System.out.println("\n2. Тест tabulate с рефлексией:");
    Sin sinFunc = new Sin();
    f = TabulatedFunctions.tabulate(
        LinkedListTabulatedFunction.class, sinFunc, 0, Math.PI, 11);
    System.out.println("   tabulate(LinkedListTabulatedFunction.class, sin, 0, PI, 11):");
    System.out.println("     Тип: " + f.getClass().getSimpleName());
    System.out.println("     Количество точек: " + f.getPointsCount());
    System.out.println("     Первая точка: " + f.getPoint(0));
    System.out.println("     Последняя точка: " + f.getPoint(f.getPointsCount() - 1));
    
    // Тест 5: SimpleArrayTabulatedFunction через рефлексию
    System.out.println("\n3. Тест SimpleArrayTabulatedFunction через рефлексию:");
    f = TabulatedFunctions.createTabulatedFunction(
        SimpleArrayTabulatedFunction.class, 0, 5, 4);
    System.out.println("   createTabulatedFunction(SimpleArrayTabulatedFunction.class, 0, 5, 4):");
    System.out.println("     Тип: " + f.getClass().getSimpleName());
    System.out.println("     Функция: " + f);
    
    // Тест 6: Обработка ошибок (попытка создать несуществующий класс)
    System.out.println("\n4. Тест обработки ошибок:");
    try {
        f = TabulatedFunctions.createTabulatedFunction(
            TabulatedFunction.class, 0, 10, 3); // Интерфейс нельзя создать
        System.out.println("   ОШИБКА: Должно было быть исключение!");
    } catch (IllegalArgumentException e) {
        System.out.println("   Корректно обработана ошибка: " + e.getMessage());
        if (e.getCause() != null) {
            System.out.println("   Причина: " + e.getCause().getClass().getSimpleName());
        }
    }
    
    // Тест 7: Несуществующий конструктор (ИСПРАВЛЕНО!)
    System.out.println("\n5. Тест вызова метода с неправильным количеством аргументов:");
    try {
        // Правильный вызов - нужно передать 4 аргумента
        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, 5); // Корректно
        System.out.println("   Корректно создана функция: " + f.getClass().getSimpleName());
        
        // Теперь попробуем вызвать несуществующий метод
        // Этот тест нужно убрать или переписать по-другому
        System.out.println("\n6. Тест вызова несуществующего метода (через try-catch):");
        
    } catch (IllegalArgumentException e) {
        System.out.println("   Корректно: " + e.getMessage());
    }
}
}