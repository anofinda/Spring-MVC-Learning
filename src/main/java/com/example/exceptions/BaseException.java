package com.example.exceptions;

/**
 * @author dongyudeng
 */
public class BaseException extends RuntimeException{
    BaseException(){
        super();
    }
    public BaseException(String message, Throwable cause){
        super(message,cause);
    }
    public BaseException(String message){
        super(message);
    }
    public BaseException(Throwable cause){
        super(cause);
    }
}
