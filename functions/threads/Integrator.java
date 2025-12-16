package functions.threads;

import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private volatile boolean running = true;
    
    public Integrator(Task task, Semaphore semaphore) {
        super("Интегратор-Поток");
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        System.out.println(getName() + ": начал работу");
        
        try {
            // Обрабатываем пока есть задания и пока не прерваны
            while (task.getLastProcessedId() < task.getTasksCount() && running) {
                
                // Проверяем прерывание
                if (isInterrupted()) {
                    throw new InterruptedException();
                }
                
                // Захватываем семафор для чтения
                semaphore.startRead();
                
                try {
                    // Проверяем, есть ли НОВОЕ задание
                    // которое еще не обработано (currentTaskId > lastProcessedId)
                    if (task.hasNewTask()) {
                        int taskId = task.getCurrentTaskId();
                        
                        // Получаем параметры задания
                        double leftX = task.getLeftX();
                        double rightX = task.getRightX();
                        double step = task.getStep();
                        
                        // Вычисляем интеграл
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
                        
                        // ПОМЕЧАЕМ КАК ОБРАБОТАННОЕ
                        task.markAsProcessed();
                    }
                } finally {
                    // Всегда освобождаем семафор
                    semaphore.endRead();
                }
                
                // Небольшая пауза, чтобы не грузить процессор
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + ": прерван");
            running = false;
        } catch (Exception e) {
            System.out.println(getName() + ": ошибка - " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println(getName() + ": завершил работу. Обработано заданий: " + 
                          task.getLastProcessedId() + " из " + task.getTasksCount());
    }
    
    public void stopIntegrator() {
        running = false;
        interrupt();
    }
}