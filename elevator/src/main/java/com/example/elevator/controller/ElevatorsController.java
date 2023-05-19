package com.example.elevator.controller;

import com.example.elevator.excptions.WrongFloorException;
import com.example.elevator.model.Building;


import com.example.elevator.model.ElevatorState;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ElevatorsController {

    private Building building;

    public ElevatorsController(Building building) {
        this.building = building;
    }

    @GetMapping("/start-simulation")
    public String startSimulation(@RequestParam int elevators, @RequestParam int floors) throws InterruptedException {
        stopSimulation();
        building = new Building();
        building.setParameters(elevators, floors);
        building.start();
        return "Simulation started";
    }

    @GetMapping("/stop-simulation")
    public String stopSimulation() throws InterruptedException {
        if (building.getState() == Thread.State.RUNNABLE){
            building.stopSimulation();
            building.join();
        }
        return "Simulation stopped";
    }

    @GetMapping("/status")
    public List<ElevatorState> getStatus(){
        return building.getElevatorStates();
    }

    @PutMapping("/pickup")
    public String requestLiftForFloor(@RequestParam int floor, @RequestParam int direction){
        try {
            building.callElevator(floor, direction);
        } catch (WrongFloorException e) {
            return e.getMessage();
        }
        return "Elevator requested";
    }

    @PutMapping("/update")
    public String updateElevatorState(@RequestParam int elevator, @RequestParam int floor) {
        try {
            building.requestElevatorToFloor(elevator, floor);
        } catch (WrongFloorException e) {
            return e.getMessage();
        }
        return "Elevator requested";
    }

}
