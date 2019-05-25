package ru.demo.soccer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.dto.StandingFilter;
import ru.demo.soccer.entities.Standing;
import ru.demo.soccer.entities.StandingStat;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class StandingService {

    private final EntityManager entityManager;

    public List<Standing> search(StandingFilter filter) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Standing> cq = cb.createQuery(Standing.class);

        Root<Standing> standing = cq.from(Standing.class);

        Predicate criteria = cb.conjunction();

        if (filter.getLeagueId() != null) {
            criteria = cb.and(criteria, cb.equal(standing.get("league").get("leagueId"), filter.getLeagueId()));
        }

        if (filter.getUpdateDate() != null) {
            criteria = cb.and(criteria, cb.equal(standing.get("lastUpdate"), filter.getUpdateDate()));
        }

        cq.select(standing).where(criteria);

        return entityManager.createQuery(cq).getResultList();
    }

    public Set<StandingStat> getStandingStat(Standing standing) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Standing> cq = cb.createQuery(Standing.class);
        Root<Standing> root = cq.from(Standing.class);
        root.fetch("standingStats");
        cq.select(root).where(cb.equal(root.get("id"), standing.getId()));
        return entityManager.createQuery(cq).getSingleResult().getStandingStats();
    }

}
