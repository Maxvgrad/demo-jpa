package ru.demo.soccer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.entities.Sync;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JobService {

    private final EntityManager entityManager;

    public Map<String, Sync> findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sync> cq = cb.createQuery(Sync.class);
        Root<Sync> root = cq.from(Sync.class);
        cq.select(root).where(cb.equal(root.get("name"), name));
        List<Sync> jobs = entityManager.createQuery(cq).getResultList();
        return jobs.stream().collect(Collectors.toMap(Sync::getName, Function.identity()));
    }

}
