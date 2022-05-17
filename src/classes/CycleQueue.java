package classes;

import interfaces.Queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CycleQueue implements Queue {
    private final static int SIZE = 10;
    private final int[] values;
    private int start;
    private int end;

    private ReentrantLock lock;
    private Condition consumer;
    private Condition producer;

    private boolean isTaken;

    public CycleQueue() {
        values = new int[SIZE];
        start = 0;
        end = 9;

        lock = new ReentrantLock();
        consumer = lock.newCondition();
        producer = lock.newCondition();

        isTaken = false;
    }

    private int nextPosition(int current){
        return ++current % SIZE;
    }

    @Override
    public void put(int val) {
        lock.lock();
        try {
            while (full())
                producer.await();

            end = ++end % SIZE;
            values[end] = val;
            log();
            consumer.signalAll();

            isTaken = false;

        } catch (InterruptedException e) {
            System.out.println("producer await.");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int get() {
        lock.lock();
        try {
            while (empty())
                consumer.await();

            start = ++start % SIZE;
            log();
            producer.signalAll();
            isTaken = true;
            return values[start];

        } catch (InterruptedException e) {
            System.out.println("Debug: consumer await.");
        } finally {
            lock.unlock();
        }
        return -1;
    }

    @Override
    public boolean full() {
        return nextPosition(end + 1) == start;
    }

    @Override
    public boolean empty() {
        return nextPosition(end) == start;
    }

    private void log() {
        System.out.println("Queue State: ");
        int sPoint = start;
        int ePoint = end;

        if (empty()) {
            System.out.println("empty");
            return;
        }
        while (!(nextPosition(ePoint) == sPoint)) {
            System.out.print(values[sPoint] + " ");
            sPoint = nextPosition(sPoint);
        }
        System.out.println();
    }

    public int getSIZE() {
        return SIZE;
    }

    public boolean getIsTaken() {
        return isTaken;
    }
}
