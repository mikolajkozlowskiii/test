package com.example.football_api.exceptions.football;

public class LeagueNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public LeagueNotFoundException(Long id){super("League " + id +" not found!");}
    public LeagueNotFoundException(String name){super("League " + name +" not found!");}
}
