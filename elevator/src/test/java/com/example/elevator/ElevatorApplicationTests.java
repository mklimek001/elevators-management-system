package com.example.elevator;

import com.example.elevator.excptions.WrongFloorException;
import com.example.elevator.model.Building;
import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorState;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElevatorApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void elevatorRequestTest(){
		Elevator elevator = new Elevator(0, 10);
		Assertions.assertFalse(elevator.isMoving());

		int floor = 3;
		elevator.requestNewFloor(floor);
		Assertions.assertTrue(elevator.stopsAtFloor(floor));
		Assertions.assertFalse(elevator.stopsAtFloor(4));
		Assertions.assertFalse(elevator.stopsAtFloor(5));
	}

	@Test
	void elevatorRequestWithDirectionTest(){
		Elevator elevator = new Elevator(0, 10);

		int floor = 6;
		int direction = 1;

		elevator.requestFloorWithDirection(floor, direction);
		Assertions.assertTrue(elevator.stopsAtFloorWithDirection(floor, direction));
		Assertions.assertTrue(elevator.stopsAtFloor(floor));
		Assertions.assertFalse(elevator.stopsAtFloorWithDirection(floor, -direction));
	}

	@Test
	void elevatorStateTest(){
		Elevator elevator = new Elevator(0, 10, 5);
		elevator.requestNewFloor(7);
		ElevatorState elevatorState = new ElevatorState(0,5,7);
		Assertions.assertEquals(elevator.getElevatorState(), elevatorState);

		elevator.requestNewFloor(9);
		elevatorState = new ElevatorState(0,5,9);
		Assertions.assertEquals(elevator.getElevatorState(), elevatorState);

		elevator.requestNewFloor(3);
		elevatorState = new ElevatorState(0,5,9);
		Assertions.assertEquals(elevator.getElevatorState(), elevatorState);
	}

	@Test
	void elevatorDistanceTest(){
		Elevator elevator = new Elevator(0, 10, 5);
		Assertions.assertEquals(elevator.timeToReachFloor(7), 2*elevator.getMovingTime());
		elevator.requestNewFloor(7);

		Assertions.assertEquals(elevator.timeToReachFloor(9), 4*elevator.getMovingTime() + elevator.getStandingTime());
		elevator.requestNewFloor(9);

		Assertions.assertEquals(elevator.timeToReachFloor(3), 10*elevator.getMovingTime() + 2*elevator.getStandingTime());
	}

	@Test
	void buildingTest() {
		Building building = new Building();
		int elevators = 4;
		int floors = 10;
		building.setParameters(elevators, floors);
		Assertions.assertEquals(building.getElevatorStates().size(), elevators);

		WrongFloorException exception = Assertions.assertThrows(WrongFloorException.class, () -> building.callElevator(10, 1));
		Assertions.assertEquals("Floor must be between 0 and " + (floors-1), exception.getMessage());

		exception = Assertions.assertThrows(WrongFloorException.class, () -> building.callElevator(-1, -1));
		Assertions.assertEquals("Floor must be between 0 and " + (floors-1), exception.getMessage());

		Assertions.assertDoesNotThrow(() -> {
			for(int i = 0; i<floors; i++){
				building.callElevator(i, 1);
			}
		});

	}



}
