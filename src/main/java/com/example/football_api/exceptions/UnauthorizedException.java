package com.example.football_api.exceptions;

public class UnauthorizedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public UnauthorizedException(){super("You are not authorized to this resource.");}
    public UnauthorizedException(String message){super("You are not authorized to "+ message);}
}
