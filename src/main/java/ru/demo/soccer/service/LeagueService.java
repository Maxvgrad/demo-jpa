package ru.demo.soccer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ru.demo.soccer.entities.League;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LeagueService {

    private final EntityManager entityManager;

    /**
     * Save all leagues
     */
    public void saveAll(List<League> leagues) {
        if (CollectionUtils.isEmpty(leagues)) {
            log.error("#saveAll: empty");
        }
        log.debug("#saveAll: {}", leagues);
        leagues.forEach(entityManager::merge);
    }
}
