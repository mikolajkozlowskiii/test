package com.example.football_api.exceptions.football;

import com.example.football_api.entities.football.League;

public class DuplicateLeagueException extends RuntimeException {
    private static final long DuplicateLeagueException = 1L;
    public DuplicateLeagueException(League league){super("League " + league +" already exists in DB.");}

}


