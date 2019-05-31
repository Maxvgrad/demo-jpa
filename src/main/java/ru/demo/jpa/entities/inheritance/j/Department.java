package ru.demo.jpa.entities.inheritance.j;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Department {

    @Id
    @TableGenerator(name = "generator", table = "generators", pkColumnName = "gen_name", pkColumnValue = "book",
                    valueColumnName = "gen_value", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
    private Long id;

    private String counry;
}
