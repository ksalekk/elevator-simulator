# Elevator Simulator

## General
Simple Java console program that simulates the working of an elevator in a building. The program is parameterized with positional arguments:
```
F  number of floors in the building,
t  unit of simulation time, i.e. the time in seconds needed to move one floor by the elevator
p  the probability of a passenger appearing in unit t on a random floor with the intention of traveling to random floor
```

The program graphically visualizes the state of the entire system every unit of time *t*, i.e. the location of the elevator, current source and destination floors, the elevator condition, the number of passengers inside, passengers waiting on each floor.

<br /><br /><br />

![Diagram UML for the simulator](./uml.png)


## Run
```
# Make sure you have installed JDK 
javac -version

cd ./elevatoor-simulator
javac -d bins ./src/edu/ksalekk/elevatorsimulator/*.java
cd ./bins

# Remember not to type .class extension
java edu/ksalekk/elevatorsimulator/ElevatorSimulatorTest
```
You can also run the program in your IDE.
