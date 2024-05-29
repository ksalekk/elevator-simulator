package edu.ksalekk.elevatorsimulator;

import java.util.concurrent.ThreadLocalRandom;

public class PassengerGenerator implements Runnable {
    private final Elevator elevator;
    private final float probability;
    private final int floors;
    private final int timeUnit;


    public PassengerGenerator(Elevator elevator, float probability, int floors, int timeUnit) {
        this.elevator = elevator;
        this.probability = probability;
        this.floors = floors;
        this.timeUnit = timeUnit;
    }

    @Override
    public void run() {
        try {
            while(true) {
                if (ThreadLocalRandom.current().nextInt(101) <= this.probability * 100) {
                    int sourceFloor = ThreadLocalRandom.current().nextInt(this.floors + 1);
                    int destinationFloor = ThreadLocalRandom.current().nextInt(this.floors + 1);
                    if (sourceFloor == destinationFloor) {
                        continue;
                    }
                    Passenger passenger = new Passenger(sourceFloor, destinationFloor);
                    this.elevator.takeElevator(passenger);
                }
                Thread.sleep(this.timeUnit * 1000L);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }
}

