package ru.demo.jpa.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import ru.demo.jpa.entities.Address;
import ru.demo.jpa.entities.Dept;
import ru.demo.jpa.entities.Employee;
import ru.demo.jpa.entities.Phone;
import ru.demo.jpa.entities.Project;
import ru.demo.jpa.entities.elementcollection.PhoneType;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VirtualCompany {

    private final EntityManager entityManager;

    public static VirtualCompany getInstance(EntityManager em) {
        return new VirtualCompany(em);
    }

    /**
     * Create virtual company with three departments and two projects.
     * Hire one manger for two engineers.
     */
    public void populate() {
        if (!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();

        populateInternal();

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private void populateInternal() {

        // departments block
        Company company = new Company(Set.of("SALES", "CEO", "HR", "R&D"), Set.of("VR", "AI"));

        Employee managerLarry = company.buildManager("Steve",
                                                     Address.builder()
                                                            .zip("3353").state("CA").street("16th St").city("San Fr")
                                                            .build(),
                                                     "R&D",
                                                     Set.of(
                                                             Phone.builder().number("758932").type(PhoneType.HOME)
                                                                  .build(),
                                                             Phone.builder().number("99783490905")
                                                                  .type(PhoneType.MOBILE).build()
                                                     ),
                                                     Set.of("Secret project")
        );

        managerLarry.setDepartment(null); // without department

        Employee managerSteve = company.buildManager("Steve",
                                                     Address.builder()
                                                            .zip("3355").state("CA").street("17th St").city("San Fr")
                                                            .build(),
                                                     "R&D",
                                                     Set.of(
                                                             Phone.builder().number("643270").type(PhoneType.HOME)
                                                                  .build(),
                                                             Phone.builder().number("99528850906")
                                                                  .type(PhoneType.MOBILE).build(),
                                                             Phone.builder().number("4038968834").type(PhoneType.WORK)
                                                                  .build()
                                                     ),
                                                     Set.of("VR")
        );

        Employee engineerJon = company.buildRegularEmployee("Jon",
                                                     Address.builder()
                                                            .zip("4855").state("CA").street("18th St").city("San Fr")
                                                            .build(),
                                                     "R&D",
                                                     Set.of(
                                                             Phone.builder().number("750328").type(PhoneType.HOME)
                                                                  .build(),
                                                             Phone.builder().number("95431307721")
                                                                  .type(PhoneType.MOBILE).build(),
                                                             Phone.builder().number("4038968835").type(PhoneType.WORK)
                                                                  .build()
                                                     ),
                                                     Set.of("AI"), managerSteve, null
        );


        Employee engeneereBob = company.buildRegularEmployee("Bob",
                                                       Address.builder()
                                                              .zip("4855").state("CA").street("20th St").city("San Fr")
                                                              .build(),
                                                             "R&D",
                                                       Set.of(
                                                               Phone.builder().number("690283").type(PhoneType.HOME)
                                                                    .build(),
                                                               Phone.builder().number("98039950086")
                                                                    .type(PhoneType.MOBILE).build(),
                                                               Phone.builder().number("4038968839").type(PhoneType.WORK)
                                                                    .build()
                                                       ),
                                                       Set.of("VR"), managerSteve, null
        );


        entityManager.persist(managerSteve);
        entityManager.persist(managerLarry);
    }

    /**
     * Internal class which represents Company and aggregates departments and projects withing itself.
     */
    private class Company {

        private final Random random = new Random(System.nanoTime());

        private Map<String, Dept> departments;
        private Map<String, Project> projects;

        /**
         * Generate two maps. Department name and Department obj, Project name and Project obj.
         *
         * @param departments - list of departments names.
         * @param projects - list of projects names.
         */
        private Company(Set<String> departments, Set<String> projects) {

            if (CollectionUtils.isEmpty(departments)) {
                departments = Collections.emptySet();
            }
            if (CollectionUtils.isEmpty(projects)) {
                projects = Collections.emptySet();
            }

            this.departments = departments.stream().map(d -> Dept.builder().name(d).build()).collect(Collectors.toMap(
                    Dept::getName, Function.identity()));

            this.projects = projects.stream().map(p -> Project.builder().name(p).build()).collect(Collectors.toMap(
                    Project::getName, Function.identity()));
        }

        /**
         * Create Manager employee. It's one manager filed is equals to null and directs is empty.
         */
        private Employee buildManager(String name, Address addr, String dept, Set<Phone> phones, Set<String> projects) {
            return buildRegularEmployee(name, addr, dept, phones, projects, null, new HashSet<>());
        }

        private Employee buildRegularEmployee(String name, Address addr, String deptName, Set<Phone> phones,
                                              Set<String> projectNames, Employee manager, Set<Employee> directs) {

            Dept dept = departments.computeIfAbsent(deptName, k -> Dept.builder().name(k).build());
            Set<Project> projects = projectNames.stream().map(p -> this.projects
                    .computeIfAbsent(p, k -> Project.builder().name(k).build())).collect(Collectors.toSet());

            Employee emp = Employee.builder()
                                   .salary(salary())
                                   .name(name)
                                   .address(addr)
                                   .department(dept)
                                   .phones(phones)
                                   .projects(projects)
                                   .manager(manager)
                                   .directs(directs)
                                   .build();

            phones.forEach(p -> p.setEmployee(emp));
            projects.forEach(p -> p.getEmployee().add(emp));

            if (manager != null) {
                manager.getDirects().add(emp);
            }

            if (directs != null && !directs.isEmpty()) {
                directs.forEach(d -> d.setManager(emp));
            }

            dept.getEmployees().add(emp);

            return emp;

        }

        /**
         * Generate random salary per year from 1 000 to 200 000 of abstract money currency.
         */
        private Long salary() {
            return (long) (random.nextInt(200) + 1) * 1000;
        }
    }
}
