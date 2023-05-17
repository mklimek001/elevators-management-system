package com.example.elevator.controller;

import com.example.elevator.model.Building;


import com.example.elevator.model.ElevatorState;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ElevatorsController {

    private final Building building;

    public ElevatorsController(Building building) {
        this.building = building;
    }

    @GetMapping("/start-simulation")
    public String startSimulation(@RequestParam int elevators, @RequestParam int floors) {
        building.setParameters(elevators, floors);
        building.start();
        return "Simulation started";
    }

    @GetMapping("/stop-simulation")
    public String stopSimulation() throws InterruptedException {
        building.stopSimulation();
        building.join();
        return "Simulation stopped";
    }

    @GetMapping("/status")
    public List<ElevatorState> getStatus(){
        return building.getElevatorStates();
    }

    @PutMapping("/pickup")
    public void requestLiftForFloor(@RequestParam int floor, @RequestParam int direction){
        building.callElevator(floor, direction);
    }

    @PutMapping("/update")
    public void updateElevatorState(@RequestParam int elevator, @RequestParam int floor){
        building.requestElevatorToFloor(elevator, floor);
    }

}
