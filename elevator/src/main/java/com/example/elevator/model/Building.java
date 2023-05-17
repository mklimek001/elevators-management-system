package com.example.elevator.model;


import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Building extends Thread{
    ArrayList<Elevator> elevators;
    int floorsNumber;

    public Building() {
        this.elevators = new ArrayList<>();
        this.floorsNumber = 0;
    }


    public void setParameters(int elevatorsNum, int floorsNum){
        for(int i = 0; i < elevatorsNum; i++){
            elevators.add(new Elevator(i));
        }
        this.floorsNumber = floorsNum;
    }


    public void callElevator(int floor){
        if(floor >= 0 && floor < floorsNumber){
            if(!wasRequestedToFloor(floor)) {
                int elevatorToRequestIdx = nearestFreeElevator(floor);
                if (elevatorToRequestIdx >= 0) {
                    elevators.get(elevatorToRequestIdx).requestNewFloor(floor);
                } else {
                    elevatorToRequestIdx = elevatorToComeFirst(floor);
                    elevators.get(elevatorToRequestIdx).requestNewFloor(floor);
                }
            }

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

    private boolean wasRequestedToFloor(int floor){
        for(Elevator elevator:elevators){
            if (elevator.stopsAtFloor(floor)){
                return true;
            }
        }
        return false;
    }

    private int elevatorToComeFirst(int floor){
        int elevatorId = -1;
        int elevatorNeededTime = floorsNumber * 20;

        for(Elevator elevator:elevators){
            if(elevator.timeToReachFloor(floor) < elevatorNeededTime){
                elevatorNeededTime = elevator.timeToReachFloor(floor);
                elevatorId = elevator.getElevatorId();
            }

        }
        return elevatorId;
    }

    public void stopSimulation(){
        for(Elevator elevator:elevators){
            elevator.stopWorking();
        }
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
