package ru.demo.soccer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.demo.soccer.dto.TeamDto;
import ru.demo.soccer.dto.VenueFilter;
import ru.demo.soccer.entities.ApiTeam;
import ru.demo.soccer.entities.Season;
import ru.demo.soccer.entities.Team;
import ru.demo.soccer.entities.Venue;
import ru.demo.soccer.service.TeamService;
import ru.demo.soccer.service.VenueService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamProcessor {

    private final TeamService teamService;
    private final VenueService venueService;

    private Season season;

    /**
     * Process all teams
     */
    public void process(List<TeamDto> teams, Season season) {
        log.debug("#process: leagues size {}", teams.size());
        this.season = season;
        teams.forEach(this::processInternal);
    }

    private void processInternal(TeamDto teamDto) {
        log.debug("#processInternal: {}", teamDto);

        ApiTeam apiTeam = teamService.findByApiTeamId(teamDto.getTeamId()).orElseGet(() -> {

            Team team = Team.builder()
                            .code(teamDto.getCode())
                            .founded(teamDto.getFounded())
                            .logo(teamDto.getLogo())
                            .name(teamDto.getName())
                            .checked(Boolean.TRUE)
                            .build();
            ApiTeam at = ApiTeam.builder().apiId(teamDto.getTeamId()).team(team).build();

            return at;
        });

        Team team = apiTeam.getTeam();

        team.getSeasons().add(season);
        season.getTeams().add(team);

        teamService.save(apiTeam);

        List<Venue> venues = venueService.search(VenueFilter.builder().name(teamDto.getVenueName()).build());

        Venue venue;

        if (CollectionUtils.isEmpty(venues)) {

            venue = Venue.builder()
                         .venueName(teamDto.getVenueName())
                         .country(season.getLeague().getCountry())
                         .venueAddress(teamDto.getVenueAddress())
                         .venueCapacity(teamDto.getVenueCapacity())
                         .venueCity(teamDto.getVenueCity())
                         .venueSurface(teamDto.getVenueSurface())
                         .build();

        } else {
            if (CollectionUtils.size(venues) > 1) {
                log.warn("#processInternal: venues constrain violation {}", venues);
                throw new IllegalStateException("Venue constrain violation");
            }
            venue = venues.stream().findFirst().get();
        }

        venue.getTeams().add(apiTeam.getTeam());

        venueService.save(venue);
    }

}
