/**
 * Task 2
 */
public class Threads {

    public static void main(String[] args) throws InterruptedException {
        ThreadPrinter firstThread = new ThreadPrinter();
        firstThread.start();
        firstThread.join();
        System.out.println("World!");
        System.out.println("World!");
        System.out.println("World!");
        System.out.println("World!");
        System.out.println("World!");
    }
}
