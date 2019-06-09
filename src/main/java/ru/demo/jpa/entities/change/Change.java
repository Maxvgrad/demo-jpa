package ru.demo.jpa.entities.change;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.demo.jpa.entities.Dept;
import ru.demo.jpa.entities.Employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Change {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "change_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @NotNull(groups = {EmployeeChange.class})
    @Null(groups = {DeptChange.class})
    @ManyToOne
    private Employee employee;


    @Null(groups = {EmployeeChange.class})
    @NotNull(groups = {DeptChange.class})
    @ManyToOne
    private Dept dept;

    @NotEmpty
    private String before;


}
