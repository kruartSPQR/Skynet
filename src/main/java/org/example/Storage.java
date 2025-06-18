package org.example;

import java.util.EnumMap;

public class Storage {
    Storage() {
        this.robotParts = new EnumMap<>(RobotPart.class);
    }

    EnumMap<RobotPart, Integer> robotParts;
    int numOfRobots;
}
