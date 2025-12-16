package functions.threads;

import functions.Function;
import functions.basic.Log;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private volatile boolean running = true;
    
    public Generator(Task task, Semaphore semaphore) {
        super("Генератор-Поток");
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        System.out.println(getName() + ": начал работу (всего заданий: " + task.getTasksCount() + ")");
        
        try {
            // Генерируем все задания по порядку
            for (int taskId = 1; taskId <= task.getTasksCount() && running; taskId++) {
                
                // Проверяем прерывание
                if (isInterrupted()) {
                    throw new InterruptedException();
                }
                
                // Ожидаем, пока интегратор обработает предыдущее задание
                // (чтобы не генерировать слишком далеко вперед)
                while (task.getLastProcessedId() < taskId - 1 && running) {
                    Thread.sleep(5);
                }
                
                // Захватываем семафор для записи
                semaphore.startWrite();
                
                try {
                    // Генерируем новое задание
                    double base = 1 + Math.random() * 9;
                    Function log = new Log(base);
                    
                    double leftX = Math.random() * 100;
                    double rightX = 100 + Math.random() * 100;
                    double step = Math.random();
                    
                    // Устанавливаем параметры
                    task.setFunction(log);
                    task.setLeftX(leftX);
                    task.setRightX(rightX);
                    task.setStep(step);
                    task.setCurrentTaskId(taskId); // Устанавливаем ID задания
                    
                    // Выводим информацию
                    System.out.printf("Source %.6f %.6f %.6f (task %d)%n", 
                        leftX, rightX, step, taskId);
                        
                } finally {
                    // Всегда освобождаем семафор
                    semaphore.endWrite();
                }
                
                // Небольшая пауза между генерациями
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + ": прерван");
            running = false;
        } catch (Exception e) {
            System.out.println(getName() + ": ошибка - " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println(getName() + ": завершил работу. Сгенерировано: " + 
                          (task.getCurrentTaskId()) + " заданий");
    }
    
    public void stopGenerator() {
        running = false;
        interrupt();
    }
}