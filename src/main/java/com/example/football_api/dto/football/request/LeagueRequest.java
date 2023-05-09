package com.example.football_api.dto.football.request;


import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class LeagueRequest {
    @Size(min = 3, max = 30)
    private final String name;
    @Size(min = 7, max = 7)
    private final String season;
    @Size(min = 3, max = 3)
    private final String country;
}
