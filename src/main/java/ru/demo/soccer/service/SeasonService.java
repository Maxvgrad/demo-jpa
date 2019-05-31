package ru.demo.soccer.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.demo.soccer.entities.ApiLeague;
import ru.demo.soccer.entities.Season;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class SeasonService {

    @PersistenceContext(unitName = "soccer")
    private EntityManager entityManager;

    public Season getById(Long id) {
        return null;
    }

    public Optional<Season> findById(Long id) {
        return null;
    }

    public Optional<ApiLeague> findByApiLeagueId(Long apiLeagueId) {
        return Optional.ofNullable(entityManager.find(ApiLeague.class, apiLeagueId));
    }
}
