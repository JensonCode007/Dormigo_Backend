package com.example.demo.exception;

public class ResourceAlredyExistsException extends RuntimeException{
    public ResourceAlredyExistsException(String message){
        super(message);
    }

}
