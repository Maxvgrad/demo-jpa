package ru.demo.jpa.topics;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.entities.Delegation;
import ru.demo.jpa.entities.Dept;
import ru.demo.jpa.entities.Employee;
import ru.demo.jpa.entities.EmployeeId;
import ru.demo.jpa.entities.inheritance.j.Department;
import ru.demo.jpa.entities.inheritance.j.ForeignAffairs;
import ru.demo.jpa.entities.inheritance.j.HomeLandSecurity;
import ru.demo.jpa.entities.inheritance.st.DomesticOrganisation;
import ru.demo.jpa.entities.inheritance.st.ForeignOrganisation;
import ru.demo.jpa.entities.inheritance.st.Organisation;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AdvancedQueryTest extends BaseJpaTests {


    @Test
    void namedNativeQuery() {
        // given
        ForeignOrganisation foreignOrganisation = ForeignOrganisation.builder()
                                                                     .inn("729109861292")
                                                                     .nza("310717929336490")
                                                                     .build();

        DomesticOrganisation domesticOrganisation = DomesticOrganisation.builder()
                                                                        .inn("538247502000")
                                                                        .ogrn("5163520949103")
                                                                        .build();

        getEntityManager().persist(foreignOrganisation);
        getEntityManager().persist(domesticOrganisation);
        commit();
        getEntityManager().clear();

        // when
        List<Organisation> organisations = getEntityManager().createNamedQuery("Organisation.findAll", Organisation.class).getResultList();

        // then
        assertTrue(organisations.stream().map(Organisation::getClass).collect(Collectors.toList())
                                .contains(ForeignOrganisation.class));
        assertTrue(organisations.stream().map(Organisation::getClass).collect(Collectors.toList())
                                .contains(DomesticOrganisation.class));
    }

    @Test
    void named_native_query_not_complete_set_of_fields() {
        // given
        Delegation delegation = Delegation.builder().serialNumber("54232A332DLD").pdf(new byte[]{3,4,5,63,3,2}).build();
        getEntityManager().persist(delegation);
        commit();
        getEntityManager().clear();

        begin();
        // when - not complete set of entity fields
        Delegation fromDb = getEntityManager().createNamedQuery("Delegation.badExampleFindAll", Delegation.class)
                                              .setParameter(1, delegation.getId())
                                              .getSingleResult();

        commit();
        getEntityManager().clear();

        Delegation fromDbWithoutSerialNumber = getEntityManager().find(Delegation.class, delegation.getId());

        // then
        assertNotNull(fromDbWithoutSerialNumber.getSerialNumber()); // TODO
    }

    // N + 1 selects problem

    /**
     * select dept0_.id as id1_8_0_, dept0_.name as name2_8_0_ from Dept dept0_ where dept0_.id in (?, ?, ?, ?)
     */

    @Test
    void batch_selects() {
        // given
        Dept dept1 = Dept.builder().name("1").build();
        Dept dept2 = Dept.builder().name("2").build();
        Dept dept3 = Dept.builder().name("3").build();
        Dept dept4 = Dept.builder().name("4").build();
        Dept dept5 = Dept.builder().name("5").build();

        Employee employee1 = Employee.builder().id(1L).country("US").department(dept1).build();
        Employee employee2 = Employee.builder().id(2L).country("US").department(dept2).build();
        Employee employee3 = Employee.builder().id(3L).country("US").department(dept3).build();
        Employee employee4 = Employee.builder().id(4L).country("US").department(dept4).build();
        Employee employee5 = Employee.builder().id(5L).country("US").department(dept5).build();
        getEntityManager().persist(employee1);
        getEntityManager().persist(employee2);
        getEntityManager().persist(employee3);
        getEntityManager().persist(employee4);
        getEntityManager().persist(employee5);

        commit();
        getEntityManager().clear();
        // when
        List<Employee> employees = getEntityManager().createQuery("select e from Employee e", Employee.class)
                                                     .getResultList();

        employees.forEach(e -> log.info("#batch_selects: {}", e.getDepartment().getName()));
        // then

    }

    // Compound PK

    @Test
    void idClass() {
        // given
        Employee employeeVano = Employee.builder().id(5L).country("RU").name("VANO").build();
        Employee employeeJon = Employee.builder().id(6L).country("EN").name("JON").build();
        getEntityManager().persist(employeeVano);
        getEntityManager().persist(employeeJon);
        commit();
        // when

        Employee fromDb = getEntityManager().find(Employee.class, EmployeeId.builder().id(employeeVano.getId())
                                                                            .country(employeeVano.getCountry())
                                                                            .build());
        // then
        assertEquals(employeeVano, fromDb);
    }

    // Inheritance type SINGLE_TABLE

    @Test
    void singleTableInheritance() {
        // given
        ForeignOrganisation foreignOrganisation = ForeignOrganisation.builder()
                                                                     .inn("729109861292")
                                                                     .nza("310717929336490")
                                                                     .build();

        DomesticOrganisation domesticOrganisation = DomesticOrganisation.builder()
                                                                        .inn("538247502000")
                                                                        .ogrn("5163520949103")
                                                                        .build();

        getEntityManager().persist(foreignOrganisation);
        getEntityManager().persist(domesticOrganisation);
        commit();
        getEntityManager().clear();

        // when
        List<Organisation> organisations = getEntityManager()
                .createQuery("select o from Organisation o", Organisation.class).getResultList();

        // then
        assertTrue(organisations.stream().map(Organisation::getClass).collect(Collectors.toList())
                                .contains(ForeignOrganisation.class));
        assertTrue(organisations.stream().map(Organisation::getClass).collect(Collectors.toList())
                                .contains(DomesticOrganisation.class));
    }

    // Inheritance type JOINED

    @Test
    void joinedTableInheritance() {
        // given
        HomeLandSecurity homeLandSecurity = HomeLandSecurity.builder()
                                                            .counry("RU")
                                                            .postCode("634049")
                                                            .build();

        ForeignAffairs foreignAffairs = ForeignAffairs.builder()
                                                      .counry("RU")
                                                      .zipCode("02134")
                                                      .build();

        getEntityManager().persist(homeLandSecurity);
        getEntityManager().persist(foreignAffairs);
        commit();
        getEntityManager().clear();

        // when
        List<Department> departments = getEntityManager()
                .createQuery("select d from Department d", Department.class).getResultList();

        // then
        assertTrue(departments.stream().map(Department::getClass).collect(Collectors.toList())
                              .contains(HomeLandSecurity.class));
        assertTrue(departments.stream().map(Department::getClass).collect(Collectors.toList())
                              .contains(ForeignAffairs.class));
    }

    // Secondary table

    @Test
    void lob_value_moved_to_secondary_table() {

    }
}
