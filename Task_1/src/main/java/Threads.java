/**
 * Task 1
 */
public class Threads {

    public static void main(String[] args) {
        ThreadPrinter firstThread = new ThreadPrinter();
        firstThread.start();
        System.out.println("World!");
        System.out.println("World!");
        System.out.println("World!");
        System.out.println("World!");
        System.out.println("World!");
    }
}
