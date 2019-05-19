package ru.demo.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class EntityManagerFactoryHolder {

    private final EntityManagerFactory entityManagerFactory;

    private final Collection<EntityManager> emRegistry = new ArrayList<>();

    /**
     * Create an instance of entity manager factory holder. Primary goal of this class is ability to track
     * and close all create entity managers by current factory holder. All active transaction of entity managers
     * will be rollback.
     */
    public static EntityManagerFactoryHolder getInstance(String persistenceUnitName) {
        Objects.requireNonNull(persistenceUnitName);
        return new EntityManagerFactoryHolder(persistenceUnitName);
    }

    private EntityManagerFactoryHolder(String persistenceUnitName) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    public EntityManager createEntityManager() {
        EntityManager em = entityManagerFactory.createEntityManager();
        emRegistry.add(em);
        return em;
    }

    /**
     * Rollback active transaction, close all entityManagers and an EntityManagerFactory.
     */
    public void close() {
        emRegistry.forEach(this::closeInternal);
        Optional.ofNullable(entityManagerFactory).ifPresent(emf -> { if (emf.isOpen()) emf.close(); });
    }

    /**
     * Rollback active transaction and close Entity manager.
     */
    private void closeInternal(EntityManager em) {
        Optional.ofNullable(em).ifPresent(eManager -> {
            if (eManager.getTransaction().isActive()) eManager.getTransaction().rollback();
            if (eManager.isOpen()) eManager.close();
        });
    }
}
