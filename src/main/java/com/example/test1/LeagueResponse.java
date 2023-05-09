package com.example.test1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
@Getter
public class LeagueResponse {
    @JsonIgnore
    private final Long id;
    private final String name;
    private final String season;
    private final String country;
    private Set<Team> teams;
}
