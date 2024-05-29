package edu.ksalekk.elevatorsimulator;

public class ElevatorSimulatorTest {
    public static int F = 5;
    public static int t = 2;
    public static float p = 0.3F;

    public static void main(String[] args) {
        if(args.length == 3) {
            F = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
            p = Float.parseFloat(args[2]);
        }

        Printer elevatorPrinter = new Printer();
        Elevator elevator =  new Elevator(F, t, elevatorPrinter);
        PassengerGenerator passengerGenerator = new PassengerGenerator(elevator, p, F, t);

        Thread elevatorThread = new Thread(elevator);
        Thread passengerGeneratorThread = new Thread(passengerGenerator);

        elevatorThread.start();
        passengerGeneratorThread.start();
    }
}
