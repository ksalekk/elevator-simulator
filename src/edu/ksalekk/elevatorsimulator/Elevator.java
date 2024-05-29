package edu.ksalekk.elevatorsimulator;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

/*
    Elevator Algorithm:
        <li> no requests => elevator stay at the current floor </li>>
        <li> elevator move in one chosen direction from src to dest </li>
        <li> if the request is on src->dest track, elevator stops and service it (take/release passengers) </li>
        <li> request is send when passenger takes elevator (src) or go inside elevator (dest) </li>
 */

public class Elevator implements Runnable, TakeableElevator, PrintableElevator {
    private final int MAX_FLOOR;
    private final int FLOOR_TO_FLOOR_TIME;

    /**
     * Stores passengers, that has not been serviced yet
     */
    private final CopyOnWriteArrayList<Passenger> waitingPassengers;

    /**
     * Stores passengers, that has not been serviced yet
     */
    private final CopyOnWriteArrayList<Passenger> insidePassengers;

    /**
     * Stores all requests (both from waiting and inside passengers)
     */
    private final ConcurrentSkipListSet<Integer> allRequests;


    public ElevatorState elevatorState;
    private int currentFloor;
    private int sourceFloor;
    private int destinationFloor;

    private final Printer elevatorPrinter;

    public Elevator(int maxFloor, int floorToFloorTime, Printer elevatorPrinter) {
        this.MAX_FLOOR = maxFloor;
        this.FLOOR_TO_FLOOR_TIME = floorToFloorTime;

        this.waitingPassengers = new CopyOnWriteArrayList<>();
        this.insidePassengers = new CopyOnWriteArrayList<>();
        this.allRequests = new ConcurrentSkipListSet<>();

        this.elevatorState = ElevatorState.STOP;

        this.currentFloor = 0;
        this.sourceFloor = 0;
        this.destinationFloor = 0;

        this.elevatorPrinter = elevatorPrinter;
    }

    /**
     * Add passenger's request to all requests list.
     * If new request is higher than current destination for moving up or lower for moving down,
     * then it will be a new destination.
     * If elevator is stopped, then change current elevator state and update its source, destination.
     * @param passenger passenger who send request to the elevator
     */
    @Override
    public void takeElevator(Passenger passenger) {
        waitingPassengers.add(passenger);
        allRequests.add(passenger.sourceFloor);

        boolean higherFloorRequest = (elevatorState == ElevatorState.MOVING_UP && destinationFloor < passenger.sourceFloor);
        boolean lowerFloorRequest = (elevatorState == ElevatorState.MOVING_DOWN && destinationFloor > passenger.sourceFloor);
        if(higherFloorRequest || lowerFloorRequest) {
            destinationFloor = passenger.sourceFloor;
        }

        if(elevatorState == ElevatorState.STOP) {
            destinationFloor = passenger.sourceFloor;
            elevatorState = destinationFloor - currentFloor > 0 ? ElevatorState.MOVING_UP : ElevatorState.MOVING_DOWN;
        }
    }

    /**
     * Take waiting passengers from current floor (if there are passengers for whom sourceFloor==currentFloor).
     * Update passengers lists and request list.
     */
    private void takePassengers() {
        for (Passenger passenger : waitingPassengers) {
            if (passenger.sourceFloor == currentFloor) {
                waitingPassengers.remove(passenger);
                insidePassengers.add(passenger);
                allRequests.add(passenger.destinationFloor);
                allRequests.remove(currentFloor);
            }
        }
    }

    /**
     * Release passengers in current floor (if there are inside passengers for whom destinationFloor==currentFloor).
     * Update inside passengers list and request list.
     */
    private void releasePassengers() {
        for (Passenger passenger : insidePassengers) {
            if (passenger.destinationFloor == currentFloor) {
                insidePassengers.remove(passenger);
                allRequests.remove(passenger.destinationFloor);
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            if(elevatorState == ElevatorState.STOP) {
                elevatorPrinter.print(this);
            }

            if(allRequests.isEmpty()) {
                try {
                    Thread.sleep(FLOOR_TO_FLOOR_TIME * 1000L);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            int step = destinationFloor - sourceFloor > 0 ? 1 : -1;
            while(currentFloor != destinationFloor) {

                elevatorPrinter.print(this);

                if(allRequests.contains(currentFloor)) {
                    releasePassengers();
                    takePassengers();
                }

                try {
                    Thread.sleep(FLOOR_TO_FLOOR_TIME*1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                currentFloor += step;
            }
            takePassengers();
            releasePassengers();
            elevatorState = ElevatorState.STOP;
            sourceFloor = currentFloor;
            if(!allRequests.isEmpty()) {
                destinationFloor = allRequests.getFirst();
                elevatorState = destinationFloor - currentFloor > 0 ? ElevatorState.MOVING_UP : ElevatorState.MOVING_DOWN;
            }
        }
    }

    @Override
    public int getMaxFloor() {
        return MAX_FLOOR;
    }

    @Override
    public CopyOnWriteArrayList<Passenger> getWaitingPassengers() {
        return waitingPassengers;
    }

    @Override
    public CopyOnWriteArrayList<Passenger> getInsidePassengers() {
        return insidePassengers;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public int getSourceFloor() {
        return sourceFloor;
    }

    @Override
    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    @Override
    public ConcurrentSkipListSet<Integer> getAllRequests() {
        return allRequests;
    }
}