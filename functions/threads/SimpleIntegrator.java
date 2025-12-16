package functions.threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": начал работу");
        
        try {
            // Обрабатываем задания по порядку
            while (task.getLastProcessedId() < task.getTasksCount()) {
                synchronized (task) {
                    // Ждем, пока появится новое задание
                    while (!task.hasNewTask()) {
                        if (task.getLastProcessedId() >= task.getTasksCount()) {
                            return; // Все задания обработаны
                        }
                        task.wait(10);
                    }
                    
                    int taskId = task.getCurrentTaskId();
                    double leftX = task.getLeftX();
                    double rightX = task.getRightX();
                    double step = task.getStep();
                    
                    try {
                        double result = Functions.integrate(
                            task.getFunction(), 
                            leftX, 
                            rightX, 
                            step
                        );
                        
                        System.out.printf("Result %.6f %.6f %.6f %.8f (task %d)%n",
                            leftX, rightX, step, result, taskId);
                            
                    } catch (IllegalArgumentException e) {
                        System.out.printf("Result %.6f %.6f %.6f ОШИБКА: %s (task %d)%n",
                            leftX, rightX, step, e.getMessage(), taskId);
                    }
                    
                    // Помечаем задание как обработанное
                    task.markAsProcessed();
                    
                    // Уведомляем генератор
                    task.notifyAll();
                }
                
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ": прерван");
            Thread.currentThread().interrupt();
        }
        
        System.out.println(Thread.currentThread().getName() + ": завершил. Обработано: " + 
                          task.getLastProcessedId() + " заданий");
    }
}