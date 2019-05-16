package ru.demo.jpa.apps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.jpa.entities.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
public class QueryApp implements Runnable {

    private final String persistenceUnitName;

    public void run() {

        EntityManagerFactory emf = null;
        EntityManager em = null;
        Random random = new Random(System.nanoTime());

        try {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            em = emf.createEntityManager();

            Account accountRub = Account.builder().currency("RUB").serialNumber(Math.abs(random.nextLong()) + "-" + Instant.now()).build();
            Account accountUsd = Account.builder().currency("USD").serialNumber(Math.abs(random.nextLong()) + "-" + Instant.now()).build();
            Account accountEur = Account.builder().currency("USD").serialNumber(Math.abs(random.nextLong()) + "-" + Instant.now()).build();

            em.persist(accountRub);
            em.persist(accountUsd);
            em.persist(accountEur);


            em.createQuery("from Account a").getResultList().forEach(a -> {
                log.info("{}", a);
            });

        } catch (Throwable ex) {
            log.error("#run: ", ex);
            throw new IllegalArgumentException(ex);
        } finally {
            Optional.ofNullable(em).ifPresent(EntityManager::close);
            Optional.ofNullable(emf).ifPresent(EntityManagerFactory::close);
        }
    }
}
