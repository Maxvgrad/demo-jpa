package ru.demo.jpa.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.entities.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class StudentServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private StudentService studentService;

    @BeforeEach
    void setUpClass() {
        emf = Persistence.createEntityManagerFactory("StudentService");
        em = emf.createEntityManager();
        studentService = new StudentService(em);
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
    void create() {
        //given
        String fName = "Max";
        String lName = "Vino";
        String email = "Max@gmail.com";
        //when
        Student student = studentService.create(fName, lName, email);
        //then
        assertNotNull(student);
        Student sFromDb = studentService.findById(student.getId());
        assertNotNull(sFromDb);
        assertEquals(fName, sFromDb.getFirstName());
        assertEquals(lName, sFromDb.getLastName());
        assertEquals(email, sFromDb.getEmail());
    }

    @Test
    void remove() {
        //given
        String fName = "Max";
        String lName = "Vino";
        String email = "Max@gmail.com";
        Student student = studentService.create(fName, lName, email);
        //when
        Student removedStudent = studentService.remove(student.getId());
        //then
        System.out.println(studentService.findById(student.getId()));
        System.out.println(student.equals(removedStudent));
        assertEquals(student, removedStudent);
    }

    @Test
    void findById() {
        //given
        String fName = "Max";
        String lName = "Vino";
        String email = "Max@gmail.com";
        Student student = studentService.create(fName, lName, email);
        //when
        Student sFromDb = studentService.findById(student.getId());
        //then
        assertEquals(student, sFromDb);
    }

    @Test
    void change() {
        //given
        String fName = "Max";
        String lName = "Vino";
        String email = "Max@gmail.com";
        String emailForUpdate = "Max@yahoo.com";
        Student student = studentService.create(fName, lName, email);
        //when
        studentService.change(student.getId(), emailForUpdate);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Student sFromDb = studentService.findById(student.getId());
        //then
        assertEquals(emailForUpdate, sFromDb.getEmail());
    }

    @Test
    void findAll() {
        //given
        String fName = "Max";
        String lName = "Vino";
        String email = "Max@gmail.com";
        Student student = studentService.create(fName, lName, email);
        //when
        List<Student> studentList = studentService.findAll();
        //then
        assertEquals(1, studentList.size());
    }
}