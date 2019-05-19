package ru.demo.jpa.apps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseApp implements Runnable {

    private final String persistenceUnitName;

    private EntityManagerFactory emf = null;
    private EntityManager em = null;

    protected void init() throws Exception {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    protected abstract void compute() throws Exception;

    @Override
    public void run() {
        log.info("#run: unit={}", persistenceUnitName);
        try {
            init();
            compute();

        } catch (Exception ex) {

            log.error("#run: ", ex);
            throw new IllegalArgumentException(ex);

        } finally {

            Optional.ofNullable(em).ifPresent(m -> {
                if (em.getTransaction().isActive()) em.getTransaction().commit();
                if (em.isOpen()) em.close();
            });

            Optional.ofNullable(emf).ifPresent(f -> {
                if (emf.isOpen()) emf.close();
            });

        }
    }

    protected EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    protected EntityManager getEntityManager() {
        return em;
    }
}
