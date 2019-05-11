package ru.demo.jpa.services;

import lombok.RequiredArgsConstructor;
import ru.demo.jpa.entities.elementcollection.NuclearPowerPlant;
import ru.demo.jpa.entities.elementcollection.Reactor;
import ru.demo.jpa.entities.elementcollection.Tvs;

import javax.persistence.EntityManager;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
public class ReactorService {

    private final EntityManager entityManager;

    private Random random = new Random(System.nanoTime());

    public Reactor create(NuclearPowerPlant npp, Set<Tvs> tvs, Set<String> tvel) {
        Reactor reactor = Reactor.builder().npp(npp).power(Math.abs(random.nextLong())).tvs(tvs).tvels(tvel).build();
        entityManager.persist(reactor);
        return reactor;
    }

    public Reactor findById(Long id) {
        return entityManager.find(Reactor.class, id);
    }
}
