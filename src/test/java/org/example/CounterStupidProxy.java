package org.example;

import java.util.ArrayList;
import java.util.List;

public class CounterStupidProxy extends ActionCounter {

    private final List<Integer> calls = new ArrayList<>();

    @Override
    public void call(int timestamp){
        synchronized (calls){
            calls.add(timestamp);
        }
        super.call(timestamp);
    }

    public int getStupidSolution(int timestamp){
        return (int) calls.stream().filter(t -> t <= timestamp && t >= timestamp - 300).count();
    }
}
