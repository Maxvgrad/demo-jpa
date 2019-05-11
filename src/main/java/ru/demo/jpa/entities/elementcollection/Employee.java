package ru.demo.jpa.entities.elementcollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = {"nuclearPowerPlant", "phones"})
@ToString(exclude = {"nuclearPowerPlant", "phones"})
public class Employee {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "reactor_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Department department;

    @ElementCollection
    @CollectionTable(name = "phone", joinColumns = @JoinColumn(name  = "employee_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "phone_type")
    @Column(name = "phone_number")
    private Map<Phone, String> phones;

    @ManyToMany()
    private Collection<NuclearPowerPlant> nuclearPowerPlant;
}
