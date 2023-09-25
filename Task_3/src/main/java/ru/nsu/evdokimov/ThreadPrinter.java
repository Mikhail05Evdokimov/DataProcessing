package ru.nsu.evdokimov;

import java.util.List;

public class ThreadPrinter extends Thread{
    private final List<String> stringList;

    public ThreadPrinter(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public void run(){
        for (String i : stringList) {
            System.out.println(i);
        }
    }
}