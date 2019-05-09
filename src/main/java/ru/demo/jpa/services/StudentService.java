package ru.demo.jpa.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.demo.jpa.entities.Student;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class StudentService {

    private final EntityManager entityManager;

    /**
     * Create a student
     */
    public Student create(String firstName, String lastName, String email) {
        Student student = Student.builder().firstName(firstName).lastName(lastName).email(email).build();
        entityManager.persist(student);
        return student;
    }

    /**
     * Remove student
     */
    public Student remove(Long id) {
        Student student = findById(id);
        Optional.ofNullable(student).ifPresent(entityManager::remove);
        return student;
    }

    /**
     * Find Student by id
     */
    public Student findById(Long id) {
        Objects.requireNonNull(id, "Student(id: null)");
        return entityManager.find(Student.class, id);
    }

    /**
     * Change email
     */
    public Student change(Long id, String email) {
        Objects.requireNonNull(email, "Email");
        Student student = findById(id);
        Optional.ofNullable(student).ifPresent(s -> s.setEmail(email));
        return student;
    }

    /**
     * Find all students
     */
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createQuery("select s from Student s where 1=1", Student.class);
        return query.getResultList();
    }


}
