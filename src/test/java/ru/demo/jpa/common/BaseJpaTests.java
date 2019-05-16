package ru.demo.jpa.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

public class BaseJpaTests {

    private String persistenceUnitName = "demo";
    private EntityManagerFactory emf;
    private EntityManager em;
    private boolean openTransaction = true;

    @BeforeEach
    protected void setUpClass() {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        if (openTransaction) em.getTransaction().begin();
    }

    @AfterEach
    protected void tearDown() {
        Optional.ofNullable(em).ifPresent(eManager -> {
            if (eManager.getTransaction().isActive()) eManager.getTransaction().commit();
            if (eManager.isOpen()) eManager.close();
        });
        Optional.ofNullable(emf).ifPresent(emFactory -> { if (emFactory.isOpen()) emFactory.close(); });
    }

    /**
     * Is begin transaction?
     */
    protected void setOpenTransaction(boolean openTransaction) {
        this.openTransaction = openTransaction;
    }

    protected void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
