package ru.demo.jpa.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.demo.jpa.util.EntityManagerFactoryHolder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class BaseJpaTests {

    private String persistenceUnitName = "demo";
    private EntityManagerFactoryHolder emfHolder;
    private EntityManager em;
    private boolean openTransaction = true;

    @BeforeEach
    protected void setUpClass() {
        emfHolder = EntityManagerFactoryHolder.getInstance(persistenceUnitName);
        em = emfHolder.createEntityManager();
        if (openTransaction) {
            begin();
        }
    }

    @AfterEach
    protected void tearDown() {
        emfHolder.close();
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
        return em.getEntityManagerFactory();
    }

    public void commit() {
        if (em.getTransaction().isActive()) em.getTransaction().commit();
    }

    public void begin() {
        if (!em.getTransaction().isActive()) em.getTransaction().begin();
    }


}
