package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public class Factory implements Runnable {
    private static final int MAX_DAILY_PARTS = 10;
    ArrayBlockingQueue<RobotPart> partsQueue;
    CyclicBarrier dayBarrier;
    CyclicBarrier nightBarrier;


    Factory(CyclicBarrier dayBarrier, CyclicBarrier nightBarrier, ArrayBlockingQueue<RobotPart> partsQueue) {
        this.dayBarrier = dayBarrier;
        this.nightBarrier = nightBarrier;
        this.partsQueue = partsQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 100; i++) {
                dayBarrier.await();
                makeParts();
                nightBarrier.await();
            }

        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    void makeParts() {
        RandomGenerator rnd = ThreadLocalRandom.current();
        int count = rnd.nextInt(MAX_DAILY_PARTS + 1);
        for (int i = 0; i < count; i++) {
            RobotPart part = RobotPart.values()[rnd.nextInt(RobotPart.values().length)];
            partsQueue.offer(part);
        }


    }
}
