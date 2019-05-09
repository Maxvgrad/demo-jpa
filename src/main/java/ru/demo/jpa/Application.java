package ru.demo.jpa;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.demo.jpa.entities.Account;
import ru.demo.jpa.entities.Student;
import ru.demo.jpa.services.StudentService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Optional;

public class Application {

    public static void main(String[] args) {

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {

            emf = Persistence.createEntityManagerFactory("demo");

            em = emf.createEntityManager();

            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            Account account = Account.builder().id(1L).serialNumber("number").currency("EUR").build();

            em.persist(account);

            transaction.commit();

        } finally {
            Optional.ofNullable(em).ifPresent(EntityManager::close);
            Optional.ofNullable(emf).ifPresent(EntityManagerFactory::close);
        }
    }
}
