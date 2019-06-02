package ru.demo.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = {"directs", "phones", "projects"})
@ToString(exclude = {"directs", "phones", "projects"})
@IdClass(EmployeeId.class)
@NamedEntityGraph(name = "Employee.partial",
                  attributeNodes = {
        @NamedAttributeNode(value = "id"),
        @NamedAttributeNode(value = "country"),
        @NamedAttributeNode(value = "department", subgraph = "dept"),
                  },
                  subgraphs = {
        @NamedSubgraph(name = "dept", attributeNodes = {
                @NamedAttributeNode(value = "name")
        })
})
public class Employee {

    @Id
    //@SequenceGenerator(name = "seq", sequenceName = "employee_id_seq", initialValue = 100, allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Id
    private String country;

    private String name;

    private Long salary;

    @ManyToOne
    private Employee manager;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<Employee> directs = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<Phone> phones = new HashSet<>();

    @ManyToMany(mappedBy = "employee", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Project> projects = new HashSet<>();

    @Embedded
    private Address address;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Dept department;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "passport_id", unique = true)
    private Passport passport;
}
