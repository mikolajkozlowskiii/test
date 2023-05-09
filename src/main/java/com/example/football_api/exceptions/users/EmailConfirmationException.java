package com.example.football_api.exceptions.users;

public class EmailConfirmationException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public EmailConfirmationException(){super("Email is already confirmed!");}
    public EmailConfirmationException(String message){super(message);}
}
