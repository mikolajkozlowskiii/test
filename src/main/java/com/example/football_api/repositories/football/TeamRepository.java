package com.example.football_api.repositories.football;

import com.example.football_api.entities.football.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
