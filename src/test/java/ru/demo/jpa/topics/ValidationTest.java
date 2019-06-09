package ru.demo.jpa.topics;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.entities.Dept;
import ru.demo.jpa.entities.Employee;
import ru.demo.jpa.entities.change.Change;
import ru.demo.jpa.entities.change.DeptChange;
import ru.demo.jpa.entities.change.EmployeeChange;

import javax.persistence.Persistence;
import javax.persistence.ValidationMode;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTest extends BaseJpaTests {

    private Validator validator;

    @BeforeEach
    void setUpClass() {
        ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                                                      .configure().messageInterpolator(new ParameterMessageInterpolator())
                                                      .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // group

    @Test
    void employeeChangeConstraintViolation() {
        // given
        Dept dept = Dept.builder()
                        .name("dept")
                        .build();

        Change change = Change.builder()
                              .before("before")
                              .dept(dept)
                              .build();
        // when
        Set<ConstraintViolation<Change>> violations = validator.validate(change, EmployeeChange.class);
        // then
        assertFalse(violations.isEmpty());
    }

    @Test
    void employeeChangeSuccess() {
        // given
        Employee employee = Employee.builder()
                                    .id(4L)
                                    .country("ru")
                                    .build();

        Change change = Change.builder()
                              .before("before")
                              .employee(employee)
                              .build();
        // when
        Set<ConstraintViolation<Change>> violations = validator.validate(change, EmployeeChange.class);
        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    void deptChangeConstraintViolation() {
        // given
        Dept dept = Dept.builder()
                        .name("dept")
                        .build();

        Change change = Change.builder()
                              .before("before")
                              .dept(dept)
                              .build();
        // when
        Set<ConstraintViolation<Change>> violations = validator.validate(change, DeptChange.class);
        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    void turn_on_validation_mode_expect_constraint_violation() {
        // given

        Dept dept = Dept.builder()
                        .name("dept")
                        .build();

        Change change = Change.builder()
                              .dept(null)
                              .build();
        // when
        Persistence.createEntityManagerFactory("demo", Map.of("javax.persistence.validation.mode", ValidationMode.CALLBACK))
                   .createEntityManager()
                   .persist(change);
        commit();
        // then

    }
}
