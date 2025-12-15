// Thread that prints numbers from 1 to 10
class NumberThread extends Thread {
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Number: " + i);
        }
    }
}

// Thread that prints squares of numbers from 1 to 10
class SquareThread implements Runnable {
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Square: " + (i * i));
        }
    }
}

public class Task1 {
	public static void main(String[] args) {

        // Create thread using Thread class
        NumberThread t1 = new NumberThread();

        // Create thread using Runnable interface
        Thread t2 = new Thread(new SquareThread());

        // Start both threads
        t1.start();
        t2.start();
    }

}
