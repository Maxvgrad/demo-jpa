package ru.demo.soccer.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.demo.soccer.dto.VenueFilter;
import ru.demo.soccer.entities.Venue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class VenueService {

    @PersistenceContext(unitName = "soccer")
    private EntityManager entityManager;

    public List<Venue> search(VenueFilter filter) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Venue> cq = cb.createQuery(Venue.class);

        Root<Venue> root = cq.from(Venue.class);

        Predicate criteria = cb.conjunction();

        if (StringUtils.isNotBlank(filter.getName())) {
            criteria = cb.and(criteria, cb.equal(root.get("venueName"), filter.getName()));
        }

        cq.select(root).where(criteria);

        return entityManager.createQuery(cq).getResultList();
    }

    public Venue save(Venue venue) {
        entityManager.persist(venue);
        return venue;
    }

}
