package ru.demo.soccer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ru.demo.soccer.dto.TeamDto;
import ru.demo.soccer.entities.League;
import ru.demo.soccer.entities.Team;
import ru.demo.soccer.entities.Venue;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TeamProcessor {

    private final EntityManager entityManager;

    private League league;

    /**
     * Process all teams
     */
    public void process(List<TeamDto> teams, League league) {
        log.debug("#process: leagues size {}", teams.size());
        this.league = league;
        teams.forEach(this::processInternal);
    }

    private void processInternal(TeamDto teamDto) {
        log.debug("#processInternal: {}", teamDto);

        Team team = Team.builder()
                        .code(teamDto.getCode())
                        .founded(teamDto.getFounded())
                        .logo(teamDto.getLogo())
                        .league(league)
                        .name(teamDto.getName())
                        .teamId(teamDto.getTeamId())
                        .build();

        entityManager.persist(team);


        List<Venue> venues = entityManager.createQuery("select v from Venue v where v.venueName = :vName", Venue.class)
                                   .setParameter("vName", teamDto.getVenueName())
                                   .getResultList();

        Venue venue;

        if (CollectionUtils.isEmpty(venues)) {

            venue = Venue.builder()
                         .venueName(teamDto.getVenueName())
                         .country(league.getCountry())
                         .venueAddress(teamDto.getVenueAddress())
                         .venueCapacity(teamDto.getVenueCapacity())
                         .venueCity(teamDto.getVenueCity())
                         .venueSurface(teamDto.getVenueSurface())
                         .build();

        } else {
            if (CollectionUtils.size(venues) > 1) {
                log.warn("#processInternal: venues constrain violation {}", venues);
            }
            venue = venues.stream().findFirst().get();
        }

        venue.getTeams().add(team);
        entityManager.persist(venue);
    }

}
