package ru.demo.jpa.entities.elementcollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"employee", "reactors"})
@ToString(exclude = {"employee", "reactors"})
@Builder
public class NuclearPowerPlant {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "reactor_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String type;

    @OneToMany(mappedBy = "npp", fetch = FetchType.LAZY)
    @OrderBy("power asc")
    private List<Reactor> reactors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "palnt_employee",
               joinColumns = @JoinColumn(name = "plant_id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "department")
    private Map<Department, Employee> employee;
}
