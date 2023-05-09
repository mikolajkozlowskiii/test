package com.example.football_api.services.football;

import com.example.football_api.dto.football.request.LeagueRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.entities.football.League;

import java.util.List;

public interface LeagueService {
    LeagueResponse save(LeagueRequest league);
    LeagueResponse update(Long leagueId, LeagueRequest league);
    LeagueResponse delete(Long id);
    LeagueResponse findById(Long id);
    List<LeagueResponse> searchLeaguesByNameSeasonCountry(String name, String season, String country);
    List<LeagueResponse> findByName(String name);
    List<LeagueResponse> findByCountry(String country);
    List<LeagueResponse> findBySeason(String season);
}
