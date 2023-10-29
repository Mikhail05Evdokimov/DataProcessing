package ru.nsu.evdokimov;

public class ChildThread extends Thread{

    private void mySort() {
        String temp;
        synchronized (Main.stringList) {
            for (int i = 0; i + 1 < Main.stringList.size(); i++) {
                for (int j = 0; j + 1 < Main.stringList.size(); j++) {
                    if (Main.stringList.get(j).compareTo(Main.stringList.get(j+1)) > 0) {
                        temp = Main.stringList.get(j);
                        Main.stringList.set(j, Main.stringList.get(j+1));
                        Main.stringList.set(j+1, temp);
                    }
                }
            }
        }
    }

    @Override
    public void run(){

        for(;;) {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("STOP");
                break;
            }
            mySort();
        }
    }
}
