package com.capstone.userservice.exception;

/**
 * @author Javaughn Stephenson
 * @since 09/08/2023
 */

public class ResourceExistsException extends RuntimeException {

    public ResourceExistsException(String message) {
        super(message);
    }
}
