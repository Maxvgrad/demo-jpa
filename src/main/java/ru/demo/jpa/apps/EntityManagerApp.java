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

@RequiredArgsConstructor
@Slf4j
public class EntityManagerApp implements Runnable {

    private final String persistenceUnitName;

    public void run() {

        EntityManagerFactory emf = null;
        EntityManager em = null;
        Random random = new Random(System.nanoTime());

        try {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            em = emf.createEntityManager();

            Account account = Account.builder().currency("RUB")
                                     .serialNumber(Math.abs(random.nextLong()) + "-" + Instant.now()).build();

            em.persist(account);

            log.info("#run: persist {}", account);

            em.getTransaction().begin();

            em.flush();
            em.getTransaction().commit();

            log.info("#run: commit {}", account);

            em.getTransaction().begin();

            account.setCurrency("USD");

            log.info("#run: change currency into {}", account.getCurrency());

            em.getTransaction().commit();

            log.info("#run: commit {}", account);

            log.info("#run: getReference ");

            em.getTransaction().begin();

            Account accountProxy = em.getReference(Account.class, account.getId());

            log.info("#run: proxy: {}", accountProxy);

            em.getTransaction().commit();

        } catch (Throwable ex) {
            log.error("#run: ", ex);
            throw new IllegalArgumentException(ex);
        } finally {
            Optional.ofNullable(em).ifPresent(EntityManager::close);
            Optional.ofNullable(emf).ifPresent(EntityManagerFactory::close);
        }
    }
}
