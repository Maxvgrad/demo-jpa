package ru.demo.soccer.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.demo.soccer.entities.Sync;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class JobService {

    @PersistenceContext(unitName = "soccer")
    private EntityManager entityManager;

    public Map<String, Sync> findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Sync> cq = cb.createQuery(Sync.class);

        Root<Sync> root = cq.from(Sync.class);

        cq.select(root).where(cb.equal(root.get("name"), name));

        List<Sync> jobs = entityManager.createQuery(cq).getResultList();

        return jobs.stream().collect(Collectors.toMap(Sync::getKey, Function.identity()));
    }

    public Sync findByNameAndValue(String name, String key) {
        return findByNameAndValue(name, key, 0L);
    }

    private Sync findByNameAndValue(String name, String key, Long initialValue) {
        Map<String, Sync> data = findByName(name);

        return data.computeIfAbsent(key, k -> {
            Sync sync = Sync.builder().name(name).key(key).value(initialValue).build();
            entityManager.persist(sync);
            return sync;
        });

    }

}
