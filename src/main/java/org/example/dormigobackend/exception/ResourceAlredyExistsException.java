package org.example.dormigobackend.exception;

public class ResourceAlredyExistsException extends RuntimeException{
    public ResourceAlredyExistsException(String message){
        super(message);
    }

}
