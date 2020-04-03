package com.fsm.Exceptions;

public class IllegalDirectoryNameException extends RuntimeException {

    public IllegalDirectoryNameException() {
        super("Directory name must not contain .");
    }

}