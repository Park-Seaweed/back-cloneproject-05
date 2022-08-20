package com.project.instagramcloneteam5.exception;

public class MemberUsernameAlreadyExistsException extends RuntimeException{

    public MemberUsernameAlreadyExistsException(String message) {
        super(message);
    }
}
