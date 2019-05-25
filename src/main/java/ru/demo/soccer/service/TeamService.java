package ru.demo.soccer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.entities.Team;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TeamService {

    private final EntityManager entityManager;

    /**
     * @param teamId - www.api-football.com
     */
    public Team getByTeamId(Long teamId) {
        return entityManager.createQuery("select t from Team t where t.teamId = :teamId", Team.class)
                            .setParameter("teamId", teamId).getSingleResult();
    }

    public Optional<Team> findByTeamId(Long teamId) {
        try {
            return Optional.ofNullable(getByTeamId(teamId));
        } catch (NoResultException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
