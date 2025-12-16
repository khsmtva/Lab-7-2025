package functions.threads;

public class Semaphore {
    private int readers = 0;      // Количество активных читателей
    private int writers = 0;      // Количество активных писателей (0 или 1)
    private int writeRequests = 0; // Количество запросов на запись
    

    public synchronized void startRead() throws InterruptedException {
        // Ждем, пока нет писателей и запросов на запись
        while (writers > 0 || writeRequests > 0) {
            wait();
        }
        readers++;
    }
    
    public synchronized void endRead() {
        readers--;
        if (readers == 0) {
            notifyAll(); // Будим ждущих писателей
        }
    }
    
    public synchronized void startWrite() throws InterruptedException {
        writeRequests++;
        
        // Ждем, пока нет читателей и других писателей
        while (readers > 0 || writers > 0) {
            wait();
        }
        
        writeRequests--;
        writers++;
    }
    
    public synchronized void endWrite() {
        writers--;
        notifyAll(); // Будим всех ждущих
    }
}