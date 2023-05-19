package com.example.elevator.model;

import com.example.elevator.excptions.WrongFloorException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class Building extends Thread{
    List<Elevator> elevators;
    int floorsNumber;

    public Building() {
        this.elevators = Collections.synchronizedList(new ArrayList<>());
        this.floorsNumber = 0;
    }


    public void setParameters(int elevatorsNum, int floorsNum){
        for(int i = 0; i < elevatorsNum; i++){
            elevators.add(new Elevator(i, floorsNum));
        }
        this.floorsNumber = floorsNum;
    }

    public void requestElevatorToFloor(int elevator, int floor) throws WrongFloorException {
        if(floor >= 0 && floor < floorsNumber){
            elevators.get(elevator).requestNewFloor(floor);
        }else{
            throw new WrongFloorException("Floor must be between 0 and " + (floorsNumber-1));
        }
    }

    public void callElevator(int floor, int direction) throws WrongFloorException {
        if(floor >= 0 && floor < floorsNumber){
            if (canBeFreeRequested(floor, direction)) {
                int elevatorToRequestIdx = nearestFreeElevator(floor);
                elevators.get(elevatorToRequestIdx).requestFloorWithDirection(floor, direction);
            }else if(!wasRequestedToFloor(floor, direction)) {
                int elevatorToRequestIdx = elevatorToComeFirst(floor, direction);
                elevators.get(elevatorToRequestIdx).requestFloorWithDirection(floor, direction);
            }
        }else{
            throw new WrongFloorException("Floor must be between 0 and " + (floorsNumber-1));
        }
    }

    private int nearestFreeElevator(int floor){
        int elevatorId = -1;
        int elevatorDistance = floorsNumber;

        for(Elevator elevator:elevators){
            if(!elevator.isMoving() && Math.abs(elevator.getCurrentFloor() - floor) < elevatorDistance){
                elevatorId = elevator.getElevatorId();
            }
        }
        return elevatorId;
    }

    private boolean wasRequestedToFloor(int floor, int direction){
        for(Elevator elevator:elevators){
            if (elevator.stopsAtFloorWithDirection(floor, direction)){
                return true;
            }
        }
        return false;
    }

    private boolean canBeFreeRequested(int floor, int direction){
        int stopsAtFloorCounter = 0;
        int freeCounter = 0;

        for(Elevator elevator:elevators){
            if (elevator.stopsAtFloorWithDirection(floor, direction)){
                stopsAtFloorCounter++;
            }
            if (!elevator.isMoving()){
                freeCounter++;
            }
        }

        return (freeCounter > elevators.size()/2 && stopsAtFloorCounter < 2)
                || (freeCounter > 0 && stopsAtFloorCounter == 0);

    }

    private int elevatorToComeFirst(int floor, int direction){
        int elevatorId = -1;
        int elevatorNeededTime = floorsNumber * 20;
        int elevatorIdOptimal = -1;
        int elevatorNeededTimeOptimal = floorsNumber * 20;
        int currNeededTime;

        for(Elevator elevator:elevators){
            currNeededTime = elevator.timeToReachFloor(floor);
            if(currNeededTime < elevatorNeededTime){
                elevatorNeededTime = currNeededTime;
                elevatorId = elevator.getElevatorId();
            }

            if(((direction < 0 && elevator.getCurrentFloor() > floor && elevator.getDestinationFloor() < floor)
                    || (direction > 0 && elevator.getCurrentFloor() < floor && elevator.getDestinationFloor() > floor))
                    && currNeededTime < elevatorNeededTimeOptimal){

                elevatorNeededTimeOptimal = currNeededTime;
                elevatorIdOptimal = elevator.getElevatorId();
            }
        }

        if(elevatorIdOptimal >= 0){
            return elevatorIdOptimal;
        }

        return elevatorId;
    }

    public void stopSimulation(){
        for(Elevator elevator:elevators){
            elevator.stopWorking();
        }
    }

    public List<ElevatorState> getElevatorStates(){
        List<ElevatorState> states = new LinkedList<>();
        for(Elevator elevator:elevators){
            states.add(elevator.getElevatorState());
        }
        return states;
    }


    @Override
    public void run() {
        for(Elevator elevator:elevators){
            elevator.start();
        }

        for (Elevator elevator:elevators){
            try {
                elevator.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
