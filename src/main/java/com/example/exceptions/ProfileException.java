package com.example.exceptions;

/**
 * @author dongyudeng
 */
public class ProfileException extends BaseException{
    ProfileException() {
        super();
    }

    public ProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileException(String message) {
        super(message);
    }

    public ProfileException(Throwable cause) {
        super(cause);
    }
}
