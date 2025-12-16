package functions.threads;

import functions.Function;
import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task;
    
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": начал работу");
        
        try {
            for (int taskId = 1; taskId <= task.getTasksCount(); taskId++) {
                synchronized (task) {
                    // Ждем, пока интегратор обработает предыдущее задание
                    // (оставляем только 1 необработанное задание впереди)
                    while (task.getCurrentTaskId() > task.getLastProcessedId() + 1) {
                        task.wait(10);
                    }
                    
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
                    task.setCurrentTaskId(taskId);
                    
                    System.out.printf("Source %.6f %.6f %.6f (task %d)%n", 
                        leftX, rightX, step, taskId);
                    
                    // Уведомляем интегратор
                    task.notifyAll();
                }
                
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ": прерван");
            Thread.currentThread().interrupt();
        }
        
        System.out.println(Thread.currentThread().getName() + ": завершил работу");
    }
}