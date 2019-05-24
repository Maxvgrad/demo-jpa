package ru.demo.jpa.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.dto.EmployeeFilter;
import ru.demo.jpa.entities.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EmployeeServiceTest extends BaseJpaTests {

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(getEntityManager());
    }

    @Test
    void searchByName() {
        // given
        Employee employee = Employee.builder().name("Jon").build();
        getEntityManager().persist(employee);
        commit();
        EmployeeFilter filter = EmployeeFilter.builder().name("Jon").build();
        // when
        List<Employee> result = employeeService.search(filter);
        // then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(employee, result.get(0));
    }
}