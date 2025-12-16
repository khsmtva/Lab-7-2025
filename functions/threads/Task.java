package functions.threads;

import functions.Function;

public class Task {
    private Function function;
    private double leftX;
    private double rightX;
    private double step;
    private int tasksCount;
    private int currentTaskId = 0;          // ID последнего сгенерированного задания
    private int lastProcessedId = 0;        // ID последнего обработанного задания
    
    public Task(int tasksCount) {
        if (tasksCount < 1) {
            throw new IllegalArgumentException("Количество заданий должно быть положительным");
        }
        this.tasksCount = tasksCount;
    }
    
    // Геттеры и сеттеры
    public synchronized Function getFunction() {
        return function;
    }
    
    public synchronized double getLeftX() {
        return leftX;
    }
    
    public synchronized double getRightX() {
        return rightX;
    }
    
    public synchronized double getStep() {
        return step;
    }
    
    public synchronized int getTasksCount() {
        return tasksCount;
    }
    
    public synchronized int getCurrentTaskId() {
        return currentTaskId;
    }
    
    public synchronized int getLastProcessedId() {
        return lastProcessedId;
    }
    
    public synchronized boolean hasNewTask() {
        return currentTaskId > lastProcessedId && currentTaskId <= tasksCount;
    }
    
    public synchronized void setFunction(Function function) {
        this.function = function;
    }
    
    public synchronized void setLeftX(double leftX) {
        this.leftX = leftX;
    }
    
    public synchronized void setRightX(double rightX) {
        this.rightX = rightX;
    }
    
    public synchronized void setStep(double step) {
        this.step = step;
    }
    
    public synchronized void setCurrentTaskId(int currentTaskId) {
        this.currentTaskId = currentTaskId;
    }
    
    public synchronized void setLastProcessedId(int lastProcessedId) {
        this.lastProcessedId = lastProcessedId;
    }
    
    public synchronized void markAsProcessed() {
        lastProcessedId = currentTaskId;
    }
}