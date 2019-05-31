package ru.demo.soccer.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.entities.League;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class LeagueService {

    @PersistenceContext(unitName = "soccer")
    private EntityManager entityManager;

    /**
     * Get league by leagueId.
     *
     * @param leagueId - league leagueId.
     *
     * @throws EntityNotFoundException - if no entity exists with specified leagueId.
     * @throws IllegalArgumentException - if specified leagueId is not valid.
     */
    public League getById(Long leagueId) throws EntityNotFoundException, IllegalArgumentException {

        if (leagueId == null || leagueId < 1) {
            throw new IllegalArgumentException("Illegal leagueId: " + leagueId);
        }

        return entityManager.createQuery("select l from League l where l.leagueId = :leagueId", League.class)
                                  .setParameter("leagueId", leagueId)
                                  .getSingleResult();
    }

    /**
     * Find Optional League by id
     *
     * @param id - league id.
     */
    public Optional<League> findById(Long id) {
        try {
            return Optional.ofNullable(getById(id));
        } catch (EntityNotFoundException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
