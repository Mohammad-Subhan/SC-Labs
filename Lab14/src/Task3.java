import java.util.concurrent.CopyOnWriteArrayList;

// Thread class for reading and writing
class ListThread extends Thread {
    CopyOnWriteArrayList<String> list;
    String threadName;

    ListThread(CopyOnWriteArrayList<String> list, String threadName) {
        this.list = list;
        this.threadName = threadName;
    }

    public void run() {
        // Writing to the list
        for (int i = 1; i <= 3; i++) {
            String value = threadName + " Item " + i;
            list.add(value);
            System.out.println(threadName + " added: " + value);
        }

        // Reading from the list
        System.out.println(threadName + " reading list:");
        for (String item : list) {
            System.out.println(item);
        }
    }
}

public class Task3 {
    public static void main(String[] args) {

        // Thread-safe list
        CopyOnWriteArrayList<String> sharedList = new CopyOnWriteArrayList<>();

        // Create multiple threads
        ListThread t1 = new ListThread(sharedList, "Thread-1");
        ListThread t2 = new ListThread(sharedList, "Thread-2");
        ListThread t3 = new ListThread(sharedList, "Thread-3");

        // Start threads concurrently
        t1.start();
        t2.start();
        t3.start();
    }
}
