package ru.demo.jpa.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.entities.Printer;
import ru.demo.jpa.entities.PrinterTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PrinterServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUpClass() {
        emf = Persistence.createEntityManagerFactory("demo");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        Optional.ofNullable(em).ifPresent(eManager -> {
            eManager.getTransaction().commit();
            eManager.close();
        });
        Optional.ofNullable(emf).ifPresent(EntityManagerFactory::close);
    }

    @Test
    void cascadeTypePersists() {
        // given
        Printer printer = Printer.builder().ip("localhost").build();
        PrinterTask task = PrinterTask.builder().documentFormat("doc").printer(printer).build();
        //when
        em.persist(task);
        //then
        assertNotNull(printer.getId());
    }

    @Test
    void cascadeTypeRemove() {
        // given
        Printer printer = Printer.builder().ip("localhost").build();
        PrinterTask taskDoc = PrinterTask.builder().documentFormat("doc").printer(printer).build();
        PrinterTask taskPdf = PrinterTask.builder().documentFormat("pdf").printer(printer).build();
        em.persist(taskDoc);
        em.persist(taskPdf);
        //when
        em.getTransaction().commit();
        em.clear();
        em.getTransaction().begin();

        printer = em.find(Printer.class, printer.getId());
        em.remove(printer);
        //then
        assertNull(em.find(Printer.class, printer.getId()));
        assertNull(em.find(PrinterTask.class, taskDoc.getId()));
        assertNull(em.find(PrinterTask.class, taskPdf.getId()));
    }

    @Test
    void cascadeTypeRemove2() {
        // given

        emf = Persistence.createEntityManagerFactory("demo");
        em = emf.createEntityManager();
        //then
        Printer printer = em.find(Printer.class, 132L);

        System.out.println(printer);
    }



    @Test
    void createQueryJpql() {
        // given
        Printer printer = Printer.builder().ip("localhost").build();
        PrinterTask taskDoc = PrinterTask.builder().documentFormat("doc").printer(printer).build();
        PrinterTask taskPdf = PrinterTask.builder().documentFormat("pdf").printer(printer).build();
        em.persist(taskDoc);
        em.persist(taskPdf);
        //then
        List<Printer> printers = em.createQuery("select p from Printer p where p.id = 132L", Printer.class).getResultList();
        System.out.println(printers);
    }
}
