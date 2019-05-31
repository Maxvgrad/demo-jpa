package ru.demo.soccer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ru.demo.soccer.dto.LeagueDto;
import ru.demo.soccer.entities.Country;
import ru.demo.soccer.entities.League;
import ru.demo.soccer.entities.Season;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LeagueProcessor {

    private final EntityManager entityManager;

    /**
     * Process all leagues
     */
    public void process(List<LeagueDto> leagues) {
        log.debug("#process: leagues size {}", leagues.size());
        leagues.forEach(this::processInternal);
    }

    private void processInternal(LeagueDto leagueDto) {
        log.debug("#processInternal: {}", leagueDto);


        List<Country> countries = entityManager
                .createQuery("select c from Country c where c.countryCode = :code", Country.class)
                .setParameter("code", leagueDto.getCountryCode()).getResultList();

        Country country;

        if (CollectionUtils.isEmpty(countries)) {
            country = Country.builder().country(leagueDto.getCountry()).countryCode(leagueDto.getCountryCode())
                             .flag(leagueDto.getFlag()).build();
            entityManager.persist(country);
        } else {
            if (CollectionUtils.size(countries) > 1) {
                log.warn("#process: countries constraint violation for country code {}", leagueDto.getCountryCode());
            }

            country = countries.stream().findFirst().get();
        }

        Season season = Season.builder().season(leagueDto.getSeason()).seasonEnd(leagueDto.getSeasonEnd())
                              .seasonStart(leagueDto.getSeasonStart())
                              .standings(leagueDto.getStandings())
                              .current(leagueDto.getCurrent())
                              .build();

        Long leagueId = Long.valueOf(leagueDto.getLeagueId());

        League league = League.builder().leagueId(leagueId).name(leagueDto.getName()).country(country)
                              //.season(season) // TODO
                              .logo(leagueDto.getLogo()).build();
        country.getLeagues().add(league);

        entityManager.persist(league);
    }

}
