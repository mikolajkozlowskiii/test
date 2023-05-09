package com.example.test1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/leagues")
@RequiredArgsConstructor
public class LeagueController {
    private final LeagueService leagueService;

    @GetMapping("/{id}")
    public ResponseEntity<LeagueResponse> findLeagueById(@PathVariable Long id){
        return ResponseEntity.ok(leagueService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LeagueResponse> createLeague(@Valid @RequestBody LeagueRequest leagueRequest){
        LeagueResponse createdLeague = leagueService.save(leagueRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/leagues/{id}")
                .buildAndExpand(createdLeague.getId()).toUri();

        return ResponseEntity.created(location).body(createdLeague);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeagueResponse> updateLeagueById(@PathVariable Long id,
                                                           @Valid @RequestBody LeagueRequest leagueInfoRequest){
        return ResponseEntity.ok(leagueService.update(id, leagueInfoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LeagueResponse> deleteLeagueById(@PathVariable Long id){
        return ResponseEntity.ok(leagueService.delete(id));
    }

    @GetMapping()
    public ResponseEntity<List<LeagueResponse>> findLeagueByNameAndCountryAndSeason(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "season", required = false) String season,
            @RequestParam(value = "country", required = false) String country
    ){
        List<LeagueResponse> leagues = leagueService.searchLeaguesByNameSeasonCountry(name, season, country);

        return ResponseEntity.ok(leagues);
    }
}
