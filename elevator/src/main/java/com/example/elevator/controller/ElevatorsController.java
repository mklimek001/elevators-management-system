package com.example.elevator.controller;

import com.example.elevator.model.Building;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        building.join();
        return "Simulation stopped";
    }
}
