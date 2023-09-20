import java.util.ArrayList;
import java.util.List;

/**
 * Task 3
 */
public class Threads {

    public static void main(String[] args) throws InterruptedException {

        List<String> firstList = new ArrayList<>();
        firstList.add("T1: Hello");
        firstList.add("T1: World");
        firstList.add("T1: !");

        List<String> secondList = new ArrayList<>();
        secondList.add("T2: Hello");
        secondList.add("T2: World");
        secondList.add("T2: !");

        List<String> thirdList = new ArrayList<>();
        thirdList.add("T3: Hello");
        thirdList.add("T3: World");
        thirdList.add("T3: !");

        List<String> fourthList = new ArrayList<>();
        fourthList.add("T4: Hello");
        fourthList.add("T4: World");
        fourthList.add("T4: !");

        ThreadPrinter firstThread = new ThreadPrinter(firstList);
        ThreadPrinter secondThread = new ThreadPrinter(secondList);
        ThreadPrinter thirdThread = new ThreadPrinter(thirdList);
        ThreadPrinter fourthThread = new ThreadPrinter(fourthList);

        firstThread.start();
        secondThread.start();
        thirdThread.start();
        fourthThread.start();
    }
}
