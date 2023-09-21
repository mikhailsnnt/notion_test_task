package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ActionCounter {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final List<Integer> calls = new ArrayList<>();

    public void call(int timestamp) {
        if (timestamp < 0)
            throw new IllegalArgumentException("Timestamp < 0");
        Lock writeLock = readWriteLock.writeLock();
        try {
            writeLock.lock();
            calls.add(timestamp);
        } finally {
            writeLock.unlock();
        }
    }


    public int getActions(int timestamp) {
        if (timestamp < 0)
            throw new IllegalArgumentException("Timestamp < 0");
        if (calls.isEmpty())
            return 0;
        Lock readLock = readWriteLock.readLock();
        try {
            readLock.lock();

            int lTimestamp = binarySearchCallLeft(Math.max(timestamp - 300, 0));

            int rTimestamp = binarySearchCallRight(timestamp);

            return rTimestamp - lTimestamp;
        } finally {
            readLock.unlock();
        }
    }

    private int binarySearchCallLeft(int t) {
        int l = 0, r = calls.size(), mid;
        while (l < r) {
            mid = (l + r) >>> 1;
            if (calls.get(mid) < t)
                l = mid + 1;
            else
                r = mid;
        }
        return l;
    }

    private int binarySearchCallRight(int t) {
        int l = 0, r = calls.size(), mid;
        while (l < r) {
            mid = (l + r) >>> 1;
            if (calls.get(mid) > t)
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }
}
