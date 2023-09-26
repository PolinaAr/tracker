package com.andersen.exception;

public class DatabaseException extends RuntimeException{

    public DatabaseException(String message){
        super(message);
    }
}
