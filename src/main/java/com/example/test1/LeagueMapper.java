package com.example.test1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LeagueMapper {
    public League map(LeagueRequest leagueRequest){
        return League.builder()
                .country(leagueRequest.getCountry())
                .name(leagueRequest.getName())
                .season(leagueRequest.getSeason())
                .build();
    }

    public LeagueResponse map(League league){
        return LeagueResponse.builder()
                .id(league.getId())
                .country(league.getCountry())
                .name(league.getName())
                .season(league.getSeason())
                .teams(league.getTeams())
                .build();
    }

    public League update(League league, LeagueRequest updateInfo){
        Optional.ofNullable(updateInfo.getName())
                .filter(name -> !name.isEmpty())
                .ifPresent(league::setName);

        Optional.ofNullable(updateInfo.getCountry())
                .filter(country -> !country.isEmpty())
                .ifPresent(league::setCountry);

        Optional.ofNullable(updateInfo.getSeason())
                .filter(season -> !season.isEmpty())
                .ifPresent(league::setSeason);

        return league;
    }

}
