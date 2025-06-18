package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<RobotPart> partsQueue = new ArrayBlockingQueue<>(10);
        CyclicBarrier dayBarrier = new CyclicBarrier(3);
        CyclicBarrier nightBarrier = new CyclicBarrier(3);
        Faction faction1 = new Faction("Wednesday", dayBarrier, nightBarrier, partsQueue);
        Faction faction2 = new Faction("World", dayBarrier, nightBarrier, partsQueue);
        Factory factory = new Factory(dayBarrier, nightBarrier, partsQueue);


        Thread factoryThread = new Thread(factory);
        Thread faction1Thread = new Thread(faction1);
        Thread faction2Thread = new Thread(faction2);

        factoryThread.start();
        faction1Thread.start();
        faction2Thread.start();

        factoryThread.join();
        faction1Thread.join();
        faction2Thread.join();

        List<Faction> factions = List.of(faction1, faction2);
        factions.stream().forEach(faction -> System.out.println(faction.getFactionName() + " army: " + faction.getStorage().numOfRobots));
        Faction winner = factions.stream().max(Comparator.comparingInt(faction -> faction.getStorage().numOfRobots)).get();

        System.out.println(winner.getFactionName() + " faction won with " + winner.getStorage().numOfRobots + " robots");


    }
}