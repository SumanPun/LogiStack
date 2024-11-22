package com.inventory.LogiStack.exceptions;

public class ApiException extends RuntimeException{

    public ApiException(String message){
        super(message);
    }
}
