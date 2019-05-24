package ru.demo.jpa.topics;

import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.entities.Dept;
import ru.demo.jpa.entities.Employee;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CriteriaApiTest extends BaseJpaTests {

    @Test
    void double_roots() {
        // given data
        Dept department = Dept.builder().name("R&D").build();
        Employee employeeDrake = Employee.builder().department(department).name("Drake").build();
        Employee employeeMarshall = Employee.builder().department(department).name("Marshall").build();
        department.getEmployees().addAll(Set.of(employeeDrake, employeeMarshall));
        getEntityManager().persist(department);
        getEntityManager().getTransaction().commit();

        // given criteria query
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Dept> cq = cb.createQuery(Dept.class);

        Root<Dept> dept = cq.from(Dept.class);
        Root<Employee> emp = cq.from(Employee.class);

        cq.select(dept)
          .distinct(true)
          .where(cb.equal(dept, emp.get("department")));

        // when
        List<Dept> result = getEntityManager().createQuery(cq).getResultList();
        // then
        assertEquals(1, result.size());
        assertEquals(department, result.get(0));
    }

    @Test
    void path_expression_chain_without_joins_on_different_table() {
        // given domain model
        Dept dept = Dept.builder().name("R&D").build();
        Employee manager = Employee.builder().name("manager").build();
        Employee staff = Employee.builder().department(dept).manager(manager).name("staff").build();
        manager.getDirects().add(staff);
        getEntityManager().persist(manager);
        commit();

        //given criteria query
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> emp = cq.from(Employee.class);
        cq.select(emp).where(cb.equal(emp.get("department").get("name"), dept.getName()));

        // when
        List<Employee> result = getEntityManager().createQuery(cq).getResultList();

        // then
        assertEquals(1, result.size());
        assertEquals(dept.getName(), result.get(0).getDepartment().getName());
    }

    @Test
    void query_without_select() {
        // given domain model
        Dept dept = Dept.builder().name("R&D").build();
        Employee manager = Employee.builder().name("manager").build();
        Employee staff = Employee.builder().department(dept).manager(manager).name("staff").build();
        manager.getDirects().add(staff);
        getEntityManager().persist(manager);
        commit();

        //given criteria query
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> emp = cq.from(Employee.class);
        cq.where(cb.equal(emp.get("department").get("name"), dept.getName()));

        // when
        List<Employee> result = getEntityManager().createQuery(cq).getResultList();

        // then
        assertEquals(1, result.size());
        assertEquals(dept.getName(), result.get(0).getDepartment().getName());
    }

    @Test
    void query_for_state_attribute() {
        // given domain model
        Employee staff = Employee.builder().name("staff").build();
        getEntityManager().persist(staff);
        commit();

        //given criteria query
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Employee> emp = cq.from(Employee.class);
        cq.select(emp.get("name")).where(cb.equal(emp.get("name"), staff.getName()));

        // when
        List<String> result = getEntityManager().createQuery(cq).getResultList();

        // then
        assertEquals(1, result.size());
    }

    @Test
    void compound_selection_tuple() {
        // given domain model
        Employee staff = Employee.builder().name("staff").build();
        getEntityManager().persist(staff);
        commit();

        //given criteria query
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Employee> emp = cq.from(Employee.class);
        cq.select(cb.tuple(emp.get("id"), emp.get("name"))).where(cb.equal(emp.get("name"), staff.getName()));

        // when
        List<Tuple> result = getEntityManager().createQuery(cq).getResultList();

        // then
        assertEquals(1, result.size());
        Tuple tuple = result.get(0);

        System.out.println("####" + tuple);
        // TODO

        assertEquals(staff.getName(), tuple.get("name", String.class));
        assertEquals(staff.getName(), tuple.get(1, String.class));
        assertEquals(staff.getId(), tuple.get("id", Long.class));
        assertEquals(staff.getId(), tuple.get(0, Long.class));
    }

    @Test
    void multiselect_form_1() {
        // given domain model
        Employee manager = Employee.builder().name("manager").build();
        Employee staff = Employee.builder().name("staff").build();
        getEntityManager().persist(staff);
        getEntityManager().persist(manager);
        commit();

        //given criteria query
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Employee> emp = cq.from(Employee.class);
        cq.multiselect(emp.get("id"), emp.get("name"));

        // when
        List<Object[]> result = getEntityManager().createQuery(cq).getResultList();

        // then
        assertEquals(2, result.size());

        List dataPool = List.of(staff.getName(), staff.getId(), manager.getName(), manager.getId());

        assertTrue(result.stream().flatMap(Arrays::stream).allMatch(dataPool::contains));
    }
}
