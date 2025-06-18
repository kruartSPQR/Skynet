package org.example;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Faction implements Runnable {
    private static final Map<RobotPart, Integer> robotProject = Map.of(
            RobotPart.HEAD, 1,
            RobotPart.TORSO, 1,
            RobotPart.HAND, 2,
            RobotPart.FEET, 2
    );
    private static final int MAX_DAILY_PARTS_TO_TAKE = 5;

    private final String factionName;
    private final CyclicBarrier dayBarrier;
    private final CyclicBarrier nightBarrier;
    private final Storage storage;
    private final ArrayBlockingQueue<RobotPart> partsQueue;

    Faction(String factionName, CyclicBarrier dayBarrier, CyclicBarrier nightBarrier, ArrayBlockingQueue<RobotPart> partsQueue) {
        this.factionName = factionName;
        this.dayBarrier = dayBarrier;
        this.nightBarrier = nightBarrier;
        this.partsQueue = partsQueue;
        this.storage = new Storage();
    }

    public String getFactionName() {
        return factionName;
    }

    public Storage getStorage() {
        return storage;
    }


    @Override
    public void run() {
        try {
            for (int i = 1; i <= 100; i++) {
                dayBarrier.await();
                nightBarrier.await();
                takeParts();
                collectRobot();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    void takeParts() {
        for (int i = 0; i < MAX_DAILY_PARTS_TO_TAKE; i++) {
            RobotPart part = partsQueue.poll();
            if (part == null)
                break;
            storage.robotParts.merge(part, 1, (a, b) -> a + b);

        }
    }

    void collectRobot() {
        for (var entry : storage.robotParts.entrySet()) {
            RobotPart part = entry.getKey();
            int needed = robotProject.get(part);
            int inStorage = storage.robotParts.get(part);
            if (inStorage < needed)
                return;
        }
        for (var entry : storage.robotParts.entrySet()) {
            RobotPart part = entry.getKey();
            int needed = robotProject.get(part);
            storage.robotParts.compute(part, (a, b) -> b - needed);
        }
        storage.numOfRobots++;

    }

}
