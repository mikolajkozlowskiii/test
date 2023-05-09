package com.example.football_api.services.football.impl;

import com.example.football_api.dto.football.request.LeagueRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.entities.football.League;
import com.example.football_api.exceptions.football.DuplicateLeagueException;
import com.example.football_api.exceptions.football.LeagueNotFoundException;
import com.example.football_api.repositories.football.LeagueRepository;
import com.example.football_api.services.football.LeagueService;
import com.example.football_api.services.football.mappers.LeagueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;
    @Override
    public LeagueResponse save(LeagueRequest leagueRequest) {
        League league = leagueMapper.map(leagueRequest);
        saveLeague(league);
        return leagueMapper.map(league);
    }
    private League saveLeague(League league) {
        boolean isLeagueExists = leagueRepository.existsLeagueByNameAndCountryAndSeason(
                league.getName(), league.getCountry(), league.getSeason()
        );
        if(isLeagueExists){
            throw new DuplicateLeagueException(league);
        }
        return leagueRepository.save(league);
    }

    @Override
    public LeagueResponse update(Long leagueId, LeagueRequest updateLeagueInfo) {
        League leagueToBeUpdated = findLeagueById(leagueId);
        League updatedLeague = leagueMapper.update(leagueToBeUpdated, updateLeagueInfo);
        saveLeague(updatedLeague);
        return leagueMapper.map(updatedLeague);
    }

    @Override
    public LeagueResponse delete(Long id) {
        League league = findLeagueById(id);
        deleteLeague(league);
        return leagueMapper.map(league);
    }

    private void deleteLeague(League league) {
        leagueRepository.delete(league);
    }

    @Override
    public LeagueResponse findById(Long id) {
        League league = findLeagueById(id);
        return leagueMapper.map(league);
    }

    @Override
    public List<LeagueResponse> searchLeaguesByNameSeasonCountry(String name, String season, String country) {
        if (name != null && season != null && country != null) {
            return List.of(findByNameAndSeasonAndCountry(name, season, country));
        } else if (name != null && season != null) {
            return findByNameAndSeason(name, season);
        } else if (name != null && country != null) {
            return findByNameAndCountry(name, country);
        } else if (season != null && country != null) {
             return findBySeasonAndCountry(season, country);
        } else if (name != null) {
            return findByName(name);
        } else if (season != null) {
            return findBySeason(season);
        } else if (country != null) {
            return findByCountry(country);
        } else {
            return findAll();
        }
    }

    private List<LeagueResponse> findAll() {
        return leagueRepository.findAll().stream().map(s -> leagueMapper.map(s)).toList();
    }

    private LeagueResponse findByNameAndSeasonAndCountry(String name, String season, String country) {
        return leagueMapper.map(searchLeagues(name, season, country));
    }


    private List<LeagueResponse> findBySeasonAndCountry(String season, String country) {
        return leagueRepository
                .findByCountryAndSeason(country, season)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private List<LeagueResponse> findByNameAndCountry(String name, String country) {
        return leagueRepository
                .findByNameAndCountry(name, country)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private List<LeagueResponse> findByNameAndSeason(String name, String season) {
        return leagueRepository
                .findByNameAndSeason(name, season)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private League searchLeagues(String name, String season, String country) {
        return leagueRepository
                .findByNameAndCountryAndSeason(name, country, season)
                .orElseThrow(() -> new LeagueNotFoundException(name));
    }

    @Override
    public List<LeagueResponse> findByName(String name) {
        return findLeagueByName(name)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    @Override
    public List<LeagueResponse> findByCountry(String country) {
        return findLeagueByCountry(country)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    @Override
    public List<LeagueResponse> findBySeason(String season) {
        return findLeagueBySeason(season)
                .stream()
                .map(leagueMapper::map)
                .toList();
    }

    private League findLeagueById(Long id) {
        return leagueRepository
                .findById(id)
                .orElseThrow(() -> new LeagueNotFoundException(id));
    }

    private List<League> findLeagueByName(String name) {
        return leagueRepository.findByName(name);
    }

    private List<League> findLeagueByCountry(String country) {
        return leagueRepository.findByCountry(country);
    }

    private List<League> findLeagueBySeason(String season) {
        return leagueRepository.findBySeason(season);
    }
}
