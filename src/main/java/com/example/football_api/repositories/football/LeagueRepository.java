package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    @Query("SELECT l FROM League l LEFT JOIN FETCH l.teams WHERE l.id = (:id)")
    Optional<League> findById(@Param("id") Long id);
    List<League> findByName(String name);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            value = "league-teams-graph")
    List<League> findByCountry(String country);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            value = "league-teams-graph")
    List<League> findBySeason(String season);
    Optional<League> findByNameAndCountryAndSeason(String name, String country, String season);
    List<League> findByNameAndCountry(String name, String country);
    List<League> findByNameAndSeason(String name, String season);
    List<League> findByCountryAndSeason(String country, String season);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            value = "league-teams-graph")
    List<League> findAll();
    List<League> findByTeamsContaining(Team team);
    List<League> findByTeamsContains(Team team);
    Boolean existsLeagueByNameAndCountryAndSeason(String name, String country, String season);
}
