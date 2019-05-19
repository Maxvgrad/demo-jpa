package ru.demo.jpa.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(exclude = "employee")
@ToString(exclude = "employee")
@Builder
public class Project {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "project_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<Employee> employee = new HashSet<>();

    //TODO: bind with a department

}
