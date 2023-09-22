package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ActionCounter {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Deque<Integer> calls = new ArrayDeque<>();

    public void call(int timestamp) {
        if (timestamp < 0)
            throw new IllegalArgumentException("Timestamp < 0");
        Lock writeLock = readWriteLock.writeLock();
        try {
            writeLock.lock();
            calls.add(timestamp);
            removePastTimestamps(timestamp); // To reduce mem usage & get query time
        } finally {
            writeLock.unlock();
        }
    }

    public int getActions(int timestamp) {
        if (timestamp < 0)
            throw new IllegalArgumentException("Timestamp < 0");
        Lock readLock = readWriteLock.readLock();
        try {
            readLock.lock();
            removePastTimestamps(timestamp);
            return calls.size();
        } finally {
            readLock.unlock();
        }
    }

    private synchronized void removePastTimestamps(int timestamp){
        while(!calls.isEmpty() && calls.peekFirst() < timestamp - 300)
            calls.removeFirst();
    }
}
