package ru.demo.soccer.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.demo.soccer.entities.ApiTeam;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class TeamService {

    @PersistenceContext(unitName = "soccer")
    private EntityManager entityManager;

    /**
     * @param teamId - www.api-football.com
     */
    public ApiTeam getByApiTeamId(Long teamId) {
        return entityManager.createQuery("select t from ApiTeam t where t.apiId = :apiTeamId", ApiTeam.class)
                            .setParameter("apiTeamId", teamId)
                            .getSingleResult();
    }

    public Optional<ApiTeam> findByApiTeamId(Long apiTeamId) {
        try {
            return Optional.ofNullable(getByApiTeamId(apiTeamId));
        } catch (NoResultException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public ApiTeam save(ApiTeam team) {
        entityManager.persist(team);
        return team;
    }
}
