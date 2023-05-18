package com.example.elevator.excptions;

public class WrongFloorException extends Exception{
    public WrongFloorException(String errorMessage) {
        super(errorMessage);
    }
}
