// Shared counter class
class Counter {
    int count = 0;

    // synchronized method to avoid race condition
    synchronized void increment() {
        count++;
    }
}

// Thread class
class CounterThread extends Thread {
    Counter counter;

    CounterThread(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            counter.increment();
        }
    }
}

public class Task2 {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        // Create three threads
        CounterThread t1 = new CounterThread(counter);
        CounterThread t2 = new CounterThread(counter);
        CounterThread t3 = new CounterThread(counter);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        t1.join();
        t2.join();
        t3.join();

        // Final counter value
        System.out.println("Final Counter Value: " + counter.count);
    }
}
