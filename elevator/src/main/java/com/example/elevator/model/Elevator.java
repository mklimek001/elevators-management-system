package com.example.elevator.model;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Elevator extends Thread{

    private static final int standingTime = 5;
    private static final int movingTime = 2;
    private final int id;
    private int currentFloor;
    private int destinationFloor;
    private int movement;
    private boolean working;
    private final SortedSet<Integer> requestedFloors;
    private final List<Integer> floorsDirections;
    private final ReadWriteLock stateChangeLock = new ReentrantReadWriteLock();
    private final Lock readLock = stateChangeLock.readLock();
    private final Lock writeLock = stateChangeLock.writeLock();

    public Elevator(int id, int floors) {
        this.id = id;
        currentFloor = 0;
        destinationFloor = 0;
        movement = 0;
        working = true;
        SortedSet<Integer> treeSet = new TreeSet<>();
        requestedFloors = Collections.synchronizedSortedSet(treeSet);
        floorsDirections = Collections.synchronizedList(new ArrayList<>(Collections.nCopies(floors, 0)));
    }

    public Elevator(int id, int floors, int currFloor) {
        this.id = id;
        currentFloor = currFloor;
        destinationFloor = currFloor;
        movement = 0;
        working = true;
        SortedSet<Integer> treeSet = new TreeSet<>();
        requestedFloors = Collections.synchronizedSortedSet(treeSet);
        floorsDirections = Collections.synchronizedList(new ArrayList<>(Collections.nCopies(floors, 0)));
    }

    public int timeToReachFloor(int floor){
        int distance = 0;
        readLock.lock();
        try {
            if(destinationFloor >= currentFloor && floor > currentFloor){
                distance += Math.abs(currentFloor - floor) * movingTime;
                for(int i = currentFloor; i<=destinationFloor; i++){
                    if(requestedFloors.contains(i)){
                        distance += standingTime;
                    }
                }
            } else if (destinationFloor <= currentFloor && floor < currentFloor) {
                distance += Math.abs(currentFloor - floor) * movingTime;
                for(int i = currentFloor; i >= destinationFloor; i--){
                    if(requestedFloors.contains(i)){
                        distance += standingTime;
                    }
                }
            } else if (destinationFloor < currentFloor && floor > currentFloor){
                distance += Math.abs((destinationFloor - currentFloor) + (destinationFloor - floor)) * movingTime;
                for(int i = destinationFloor; i < floor; i++){
                    if(requestedFloors.contains(i)){
                        distance += standingTime;
                    }
                }
            } else{
                distance += Math.abs((destinationFloor - currentFloor) + (destinationFloor - floor)) * movingTime;
                for(int i = destinationFloor; i > floor; i--){
                    if(requestedFloors.contains(i)){
                        distance += standingTime;
                    }
                }
            }
        }finally {
            readLock.unlock();
        }
        return distance;
    }

    public void requestNewFloor(int floor){
        writeLock.lock();
        try {
            requestedFloors.add(floor);
            if (movement == 0) {
                movement = floor - currentFloor;
            }
            updateDestinationFloor();
        }finally {
            writeLock.unlock();
        }
    }

    public void requestFloorWithDirection(int floor, int direction){
        requestNewFloor(floor);
        floorsDirections.set(floor, direction);
    }
    
    public boolean stopsAtFloorWithDirection(int floor, int direction){
        if (stopsAtFloor(floor)) {
            return (floorsDirections.get(floor) * direction > 0)
                    || (floorsDirections.get(floor) == 0 && (floor - currentFloor) * direction > 0);
        }
        return false;
    }

    private void floorReached(int floor){
        floorsDirections.set(floor, 0);
        writeLock.lock();
        try {
            if (requestedFloors.remove(floor) && destinationFloor == floor) {
                if (requestedFloors.isEmpty()) {
                    movement = 0;
                } else {
                    movement = -movement;
                    updateDestinationFloor();
                }
            }
        }finally {
            writeLock.unlock();
        }
    }

    private void updateDestinationFloor() {
        writeLock.lock();
        try {
            if (movement > 0) {
                destinationFloor = requestedFloors.last();
            } else {
                destinationFloor = requestedFloors.first();
            }
        }finally {
            writeLock.unlock();
        }
    }

    public boolean stopsAtFloor(int floor){
        return requestedFloors.contains(floor);
    }

    private void moveElevator(){
        writeLock.lock();
        try {
            if(movement > 0){
                currentFloor++;
            }else if (movement < 0){
                currentFloor--;
            }
        }finally {
            writeLock.unlock();
        }
    }

    public boolean isMoving(){
        int currMovement;
        readLock.lock();
        try {
            currMovement = movement;
        }finally {
            readLock.unlock();
        }
        return currMovement != 0;
    }

    public int getElevatorId() {
        return id;
    }

    public int getCurrentFloor() {
        int tempCurrFloor;
        readLock.lock();
        try {
            tempCurrFloor = currentFloor;
        }finally {
            readLock.unlock();
        }
        return tempCurrFloor;
    }

    public int getMovingTime(){
        return movingTime;
    }

    public int getStandingTime(){
        return standingTime;
    }

    public void stopWorking(){
        working = false;
    }

    public int getDestinationFloor(){
        int tempDestFloor;
        readLock.lock();
        try {
            tempDestFloor = destinationFloor;
        }finally {
            readLock.unlock();
        }
        return tempDestFloor;
    }

    public ElevatorState getElevatorState(){
        return new ElevatorState(id, currentFloor, destinationFloor);
    }

    @Override
    public void run() {
        while(working){
            try{
                if(isMoving()){
                    if(stopsAtFloor(getCurrentFloor())){
                        Thread.sleep(1000*standingTime);
                        floorReached(getCurrentFloor());
                    }
                    Thread.sleep(1000*movingTime);
                    moveElevator();

                }else{
                    Thread.sleep(10);
                }
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
