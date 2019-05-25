package ru.demo.soccer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ru.demo.soccer.dto.StandingDto;
import ru.demo.soccer.dto.StandingFilter;
import ru.demo.soccer.entities.League;
import ru.demo.soccer.entities.Standing;
import ru.demo.soccer.entities.StandingStat;
import ru.demo.soccer.entities.Team;
import ru.demo.soccer.entities.mapper.StandingDtoToStandingMapper;
import ru.demo.soccer.entities.mapper.StandingDtoToStandingStatMapper;
import ru.demo.soccer.service.StandingService;
import ru.demo.soccer.service.TeamService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class StandingProcessor {

    private final EntityManager entityManager;
    private final StandingDtoToStandingStatMapper standingStatMapper;
    private final StandingDtoToStandingMapper standingMapper;
    private final StandingService service;
    private final TeamService teamService;

    private League league;

    /**
     * Process all teams
     */
    public void process(List<StandingDto> dtos, League league) {
        log.debug("#process: data size {}", dtos.size());
        this.league = entityManager.merge(league);

        dtos.forEach(this::processInternal);
    }

    private void processInternal(StandingDto standingDto) {
        log.debug("#processInternal: {}", standingDto);

        List<Standing> standings = service
                .search(StandingFilter.builder().leagueId(league.getLeagueId()).updateDate(standingDto.getLastUpdate())
                                      .build());

        Standing standing;

        if (CollectionUtils.isEmpty(standings)) {
            standing = standingMapper.standingDtoToStanding(standingDto);
            standing.setLeague(league);
            entityManager.persist(standing);
        } else {
            if (CollectionUtils.size(standings) > 1) {
                log.error("#processInternal: constrain violation", standings);
                throw new IllegalStateException("Constraint violation!");
            }
            standing = CollectionUtils.extractSingleton(standings);
        }

        Optional<Team> team = teamService.findByTeamId(standingDto.getTeamId());

        StandingStat standingStat = standingStatMapper.standingDtoToStandingStat(standingDto);
        standingStat.setTeam(team.orElse(null));

        standing.getStandingStats().add(standingStat);

        entityManager.persist(standing);
    }

}
