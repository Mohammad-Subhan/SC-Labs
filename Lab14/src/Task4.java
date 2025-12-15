import java.util.Random;

// Bank account class
class BankAccount {
    private int balance = 1000; // initial balance

    // synchronized deposit method
    synchronized void deposit(int amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName()
                + " deposited " + amount + " | Balance: " + balance);
    }

    // synchronized withdrawal method
    synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName()
                    + " withdrew " + amount + " | Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName()
                    + " tried to withdraw " + amount + " | Insufficient balance");
        }
    }

    int getBalance() {
        return balance;
    }
}

// Client thread
class Client extends Thread {
    BankAccount account;
    Random random = new Random();

    Client(BankAccount account, String name) {
        super(name);
        this.account = account;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            int amount = random.nextInt(500) + 100;

            if (random.nextBoolean()) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }
        }
    }
}

public class Task4 {
    public static void main(String[] args) throws InterruptedException {

        BankAccount account = new BankAccount();

        // Create multiple client threads
        Client c1 = new Client(account, "Client-1");
        Client c2 = new Client(account, "Client-2");
        Client c3 = new Client(account, "Client-3");

        // Start threads
        c1.start();
        c2.start();
        c3.start();

        // Wait for all clients to finish
        c1.join();
        c2.join();
        c3.join();

        // Final balance
        System.out.println("\nFinal Account Balance: " + account.getBalance());
    }
}
