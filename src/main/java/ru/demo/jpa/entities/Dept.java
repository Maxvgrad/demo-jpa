package ru.demo.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "employees")
@ToString(exclude = "employees")
@Builder
@BatchSize(size = 4) // solve the N+1 selects problem
public class Dept {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "dept_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();
}
