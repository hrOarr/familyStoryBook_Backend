package com.astrodust.familyStoryBook_backend.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String msg){
        super(msg);
    }
}
