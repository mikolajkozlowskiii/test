package com.example.football_api.exceptions.users;

public class ConfirmedEmailException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public ConfirmedEmailException(){super("Email is already confirmed!");}
}
