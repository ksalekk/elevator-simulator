package edu.ksalekk.elevatorsimulator;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public interface PrintableElevator {
    int getMaxFloor();
    CopyOnWriteArrayList<Passenger> getWaitingPassengers();
    CopyOnWriteArrayList<Passenger> getInsidePassengers();
    int getCurrentFloor();
    int getSourceFloor();
    int getDestinationFloor();
    ElevatorState getElevatorState();
    ConcurrentSkipListSet<Integer> getAllRequests();



}
