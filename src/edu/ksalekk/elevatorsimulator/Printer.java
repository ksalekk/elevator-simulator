package edu.ksalekk.elevatorsimulator;

public class Printer {
    private final StringBuilder stringBuilder;

    public Printer() {
        stringBuilder = new StringBuilder();
    }

    private void appendWaitingPassengerSymbol(Passenger passenger) {
        stringBuilder.append("(")
                .append(passenger.sourceFloor)
                .append("-->")
                .append(passenger.destinationFloor)
                .append(")");
    }

    private void appendElevatorSymbol(PrintableElevator elevator) {
        stringBuilder.append("[")
                .append(elevator.getElevatorState().name())
                .append(" | (")
                .append(elevator.getSourceFloor())
                .append("-->")
                .append(elevator.getDestinationFloor())
                .append(") | ")
                .append(elevator.getInsidePassengers().size())
                .append(" passengers]\t");
    }

    public void print(PrintableElevator elevator) {
        stringBuilder.append("ALL REQUESTS: ").append(elevator.getAllRequests()).append("\n");
        for(int i = elevator.getMaxFloor(); i>= 0; i--) {
            stringBuilder.append(i).append("\t");

            if(i == elevator.getCurrentFloor()) {
                appendElevatorSymbol(elevator);
            } else {
                stringBuilder.append("_____________________________________\t");
            }

            for(Passenger passenger : elevator.getWaitingPassengers()) {
                if(passenger.sourceFloor == i) {
                    appendWaitingPassengerSymbol(passenger);
                }
            }
            stringBuilder.append("\n");
        }

        System.out.println(stringBuilder);
        stringBuilder.delete(0, stringBuilder.length()-1);
    }
}
