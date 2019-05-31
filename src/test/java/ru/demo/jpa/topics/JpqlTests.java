package ru.demo.jpa.topics;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.collection.internal.PersistentSet;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.dtos.SerialNumCurrencyHolder;
import ru.demo.jpa.entities.Account;
import ru.demo.jpa.entities.Dept;
import ru.demo.jpa.entities.Employee;
import ru.demo.jpa.entities.EmployeeId;
import ru.demo.jpa.entities.Passport;
import ru.demo.jpa.entities.Phone;
import ru.demo.jpa.entities.Project;
import ru.demo.jpa.entities.elementcollection.PhoneType;
import ru.demo.jpa.utils.VirtualCompany;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Six possible clauses to be used in a select query: SELECT, FROM, WHERE, ORDER BY, GROUP BY and HAVING
 *
 */

@Slf4j
class JpqlTests extends BaseJpaTests {

    //named query

    @Test
    void createNamedQuery() {
        // given
        Account account = Account.builder().serialNumber("sn - " + Instant.now()).currency("USD").build();
        getEntityManager().persist(account);
        getEntityManager().flush();
        // when
        List<Account> accounts = getEntityManager().createNamedQuery("Account.findByCurrency", Account.class)
                                                   .setParameter("currency", account.getCurrency()).getResultList();
        //then
        assertTrue(accounts.stream().map(Account::getId).collect(Collectors.toList()).contains(account.getId()));
    }

    // dynamic named query

    @Test
    void createDynamicNamedQuery() {
        // given
        String query = "select a from Account a where a.serialNumber like 'sn - %'";
        String queryName = "Account.findBySerialNumber";

        Account account = Account.builder().serialNumber("sn - " + Instant.now()).currency("USD").build();
        getEntityManager().persist(account);
        getEntityManager().flush();

        // when
        Query q = getEntityManager().createQuery(query);
        getEntityManagerFactory().addNamedQuery(queryName, q);
        List<Account> accounts = getEntityManager().createNamedQuery(queryName, Account.class).getResultList();
        //then
        assertTrue(accounts.stream().map(Account::getId).collect(Collectors.toList()).contains(account.getId()));
    }

    //flush mode


    /**
     * COMMIT - tells the provider that queries do not overlap with changed data in PC.
     */
    @Test
    void flushModeCommit() {
        // given
        Account account = Account.builder().serialNumber("sn - " + Instant.now()).currency("USD").build();
        getEntityManager().persist(account);
        // when
        List<Account> accounts = getEntityManager().createNamedQuery("Account.findByCurrency", Account.class)
                                                   .setFlushMode(FlushModeType.COMMIT)
                                                   .setParameter("currency", account.getCurrency()).getResultList();
        // then
        assertFalse(accounts.stream().map(Account::getId).collect(Collectors.toList()).contains(account.getId()));
    }


    /**
     * AUTO - tells the provider do not overlap with changed data. This setting will ensure that pending transactional
     * changes are included to query results.
     */
    @Test
    void flushModeAuto() {
        // given
        Account account = Account.builder().serialNumber("sn - " + Instant.now()).currency("USD").build();
        getEntityManager().persist(account);
        // when
        List<Account> accounts = getEntityManager().createNamedQuery("Account.findByCurrency", Account.class)
                                                   .setFlushMode(FlushModeType.AUTO)
                                                   .setParameter("currency", account.getCurrency()).getResultList();
        // then
        assertTrue(accounts.stream().map(Account::getId).collect(Collectors.toList()).contains(account.getId()));
    }

    // parameters types

    @Test
    void parameterTypesWithDynamicQuery() {
        // given
        String query = "select a from Account a where a.openDate between :dFrom and :dTo";

        Account account = Account.builder().serialNumber("sn - " + Instant.now()).currency("USD")
                                 .openDate(LocalDateTime.now()).build();
        getEntityManager().persist(account);
        getEntityManager().flush();
        // when
        Query q = getEntityManager().createQuery(query);
        List<Account> accounts = getEntityManager().createQuery(query, Account.class)
                                                   .setParameter("dFrom", LocalDateTime.now().minus(10, ChronoUnit.SECONDS))
                                                   .setParameter("dTo", LocalDateTime.now())
                                                   .getResultList();
        //then
        assertTrue(accounts.stream().map(Account::getId).collect(Collectors.toList()).contains(account.getId()));
    }

    @Test
    void orderQueryWithDynamicQuery() {
        // given
        String query = "select a from Account a order by a.serialNumber";
        // when
        List<Account> accounts = getEntityManager().createQuery(query, Account.class).getResultList();
        //then
        accounts.forEach(System.out::println);
    }

    @Test
    void nonUniqResultEx() {
        // given
        String query = "select a from Account a order by a.serialNumber";
        // when - then
        assertThrows(NonUniqueResultException.class, () -> getEntityManager().createQuery(query, Account.class).getSingleResult());
    }

    @Test
    void noResultEx() {
        // given
        String query = "select a from Account a where a.serialNumber = 'not exists'"; //no results
        // when - then
        assertThrows(NoResultException.class, () -> getEntityManager().createQuery(query, Account.class).getSingleResult());
    }

    @Test
    void reusableQuery() {
        // given
        String queryStr = "select a from Account a";
        TypedQuery<Account> query = getEntityManager().createQuery(queryStr, Account.class);
        // when
        List<Account> list1 = query.getResultList();
        List<Account> list2 = query.getResultList();
        // then
        assertEquals(list1, list2);
    }

    @Test
    void resultStream() {
        // given
        Account account = Account.builder().serialNumber("sn - " + Instant.now()).currency("USD").balance(440L).build();
        getEntityManager().persist(account);
        getEntityManager().flush();

        String queryStr = "select a from Account a";
        TypedQuery<Account> query = getEntityManager().createQuery(queryStr, Account.class);
        // when
        Stream<Account> stream = query.getResultStream();
        // then
        stream.map(Account::getBalance).forEach(System.out::println);
    }

    @Test
    void specialTypeArrayOfObjects() {
        // given
        String queryStr = "select a.serialNumber, a.currency from Account a";
        Query query = getEntityManager().createQuery(queryStr);
        // when
        Stream stream = query.getResultStream();
        // then
        stream.forEach(obj -> {
            Object[] vals = (Object[]) obj;
            System.out.println("Serial number: " + vals[0] + ", currency: " + vals[1]);
        });
    }

    @Test
    void constructorExpression() {
        // given
        String queryStr = "select NEW ru.demo.jpa.dtos.SerialNumCurrencyHolder(a.serialNumber, a.currency) from Account a";
        TypedQuery<SerialNumCurrencyHolder> query = getEntityManager().createQuery(queryStr, SerialNumCurrencyHolder.class);
        // when
        Stream<SerialNumCurrencyHolder> stream = query.getResultStream();
        // then
        stream.forEach(System.out::println);
    }

    @Test
    void pagination() {
        // given
        Account account1 = buildAccount("USD");
        Account account2 = buildAccount("EUR");
        Account account3 = buildAccount("RUB");
        Account account4 = buildAccount("CD");
        getEntityManager().persist(account1);
        getEntityManager().persist(account2);
        getEntityManager().persist(account3);
        getEntityManager().persist(account4);
        getEntityManager().flush();

        int pageSize = 1;
        // when
        Long maxRows = getEntityManager().createNamedQuery("Account.countAll", Long.class).getSingleResult();

        for (int page = 0; page < maxRows / pageSize; page++) {
            List<Account> results = getEntityManager().createQuery("select a from Account a", Account.class)
                                                      .setFirstResult(page * pageSize)
                                                      .setMaxResults(pageSize)
                                                      .getResultList();
            //then
            assertEquals(pageSize, results.size());
        }
    }

    @Test
    void getQueryHints() {
        getEntityManager().createQuery("select a from Account a").getHints().forEach((k, v) ->
            System.out.println("Key: " + k + " , value: " + v));
    }

    // bulk update


    @Test
    void bulkUpdatePcOverlapTheUpdate() {
        // given
        Account account = buildAccount("KZ");
        getEntityManager().persist(account);

        // when
        account.setCurrency("AD");

        int rows = getEntityManager().createQuery("update Account a set a.currency = 'USD' where a.currency = 'KZ'")
                                     .executeUpdate();

        getEntityManager().getTransaction().commit();
        getEntityManager().clear();
        // then
        account = getEntityManager().find(Account.class, account.getId());
        assertEquals("AD", account.getCurrency()); // persistence context overlap the updated data in DB
    }

    @Test
    void bulkUpdateAutoFlushMode() {
        // given
        Account account = buildAccount("KZ");
        getEntityManager().persist(account);

        // when
        account.setCurrency("AD");
        getEntityManager().flush();

        getEntityManager().getTransaction().commit();
        getEntityManager().getTransaction().begin();

        int rows = getEntityManager().createQuery("update Account a set a.currency = 'USD' where a.currency = 'AD'")
                                     .executeUpdate();
        getEntityManager().refresh(account);
        // then
        account = getEntityManager().find(Account.class, account.getId());
        assertEquals("USD", account.getCurrency());
    }

    @Test
    void jpql() {

        String jpql = "select e.name" // state field path expression
                + "e.department" // single-value association
                + "e.directs" // collection - value association
                + "from Employee e";

    }

    @Test
    void path_expression_chain_without_joins() {
        // given
        Employee manager = Employee.builder().name("manager").build();
        Employee staff = Employee.builder().manager(manager).name("staff").build();
        manager.getDirects().add(staff);
        getEntityManager().persist(manager);
        commit();

        // when
        List<String> result = getEntityManager()
                .createQuery("select distinct e.manager.name from Employee e where e.manager is not null", String.class)
                .getResultList();
        // then
        assertEquals(1, result.size());
        assertEquals(manager.getName(), result.get(0));
    }

    @Test
    void path_expression_chain_without_joins_on_different_table() {
        // given
        Dept dept = Dept.builder().name("R&D").build();
        Employee manager = Employee.builder().name("manager").build();
        Employee staff = Employee.builder().department(dept).manager(manager).name("staff").build();
        manager.getDirects().add(staff);
        getEntityManager().persist(manager);
        commit();

        // when
        List<String> result = getEntityManager()
                .createQuery("select distinct e.department.name from Employee e where e.manager is not null",
                             String.class)
                .getResultList();
        // then
        assertEquals(1, result.size());
        assertEquals(dept.getName(), result.get(0));
    }

    @Test
    void distinct() {
        // given
        Employee employeeBod1 = Employee.builder().name("Bob").build();
        Employee employeeBod2 = Employee.builder().name("Bob").build();

        getEntityManager().persist(employeeBod1);
        getEntityManager().persist(employeeBod2);

        getEntityManager().getTransaction().commit();
        getEntityManager().getTransaction().begin();
        // when
        List result = getEntityManager().createQuery("select distinct e.name from Employee e").getResultList();
        // then
        assertEquals(1, result.size());
    }

    @Test
    void implicitJoin() {
        // given
        Employee employee = Employee.builder().name("Bob").build();

        Phone p1 = Phone.builder().employee(employee).number("643270").type(PhoneType.HOME).build();
        Phone p2 = Phone.builder().employee(employee).number("89528850906").type(PhoneType.MOBILE).build();
        Phone p3 = Phone.builder().employee(employee).number("4038968834").type(PhoneType.WORK).build();

        Set<Phone> phones = Set.of(p1, p2, p3);

        employee.setPhones(phones);

        phones.forEach(getEntityManager()::persist);

        getEntityManager().getTransaction().commit();
        getEntityManager().getTransaction().begin();
        // when
        List result = getEntityManager().createQuery("select p from Employee e, Phone p where e.id = ?1")
                                        .setParameter(1, employee.getId()).getResultList();
        // then
        assertEquals(3, result.size());
    }

    @Test
    void explicitJoin() {
        // given
        Employee employee = Employee.builder().name("Bob").build();

        Phone p1 = Phone.builder().employee(employee).number("643270").type(PhoneType.HOME).build();
        Phone p2 = Phone.builder().employee(employee).number("89528850906").type(PhoneType.MOBILE).build();
        Phone p3 = Phone.builder().employee(employee).number("4038968834").type(PhoneType.WORK).build();

        Set<Phone> phones = Set.of(p1, p2, p3);

        employee.setPhones(phones);

        phones.forEach(getEntityManager()::persist);

        getEntityManager().getTransaction().commit();
        getEntityManager().getTransaction().begin();
        // when
        List result = getEntityManager().createQuery("select p from Employee e join e.phones p where e.id = ?1")
                                        .setParameter(1, employee.getId()).getResultList();
        // then
        assertEquals(3, result.size());
    }

    @Test
    void findManager() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        // when
        List<Employee> managers = getEntityManager()
                .createQuery("select e from Employee e where e.directs is not empty", Employee.class).getResultList();
        // then
        assertTrue(managers.stream().allMatch(e -> e.getManager() == null));
    }

    @Test
    void findManagerWithingDepartment() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        // when
        List<Employee> managers = getEntityManager()
                .createQuery("select e from Employee e, Dept d where e.directs is not empty", Employee.class).getResultList();
        // then
        assertTrue(managers.stream().allMatch(e -> e.getManager() == null && e.getDepartment() != null));
    }

    @Test
    void leftJoin() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        // when
        List<Employee> managers = getEntityManager()
                .createQuery("select e from Employee e left join e.department d", Employee.class).getResultList();
        // then
        log.info("#leftJoin: {}", managers);
        assertTrue(managers.stream().anyMatch(e -> e.getDepartment() == null));
        assertFalse(managers.stream().allMatch(e -> e.getDepartment() == null));
    }

    @Test //TODO: fetch eagerly??????
    void leftJoinOutOfTransaction() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        getEntityManager().getTransaction().commit();
        getEntityManager().clear();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e join e.phones", Employee.class)
                .setFlushMode(FlushModeType.COMMIT)
                .getResultList();
        // then
        log.info("#leftJoinOutOfTransaction:");
        result.forEach(r -> {
            log.info("\t# {}", r);
            log.info("\t\t - {}", r.getPhones());
            log.info("\t\t - {}", r.getDirects());
            log.info("\t\t - {}", r.getProjects());
        });
        assertTrue(result.stream().allMatch(e -> CollectionUtils.isEmpty(e.getPhones())));
    }

    @Test
    void leftOuterJoin() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        // when
        List<Employee> managers = getEntityManager()
                .createQuery("select e from Employee e left outer join Dept d on (e.department = d.id)", Employee.class).getResultList();
        // then
        assertFalse(managers.isEmpty());
        assertTrue(managers.stream().anyMatch(e -> e.getDepartment() == null));
    }

    @Test
    void joinFetch() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        // when
        List<Employee> managers = getEntityManager()
                .createQuery("select e from Employee e join featch Dept d on (e.department = d.id)", Employee.class).getResultList();
        // then
        assertFalse(managers.isEmpty());
        assertTrue(managers.stream().anyMatch(e -> e.getDepartment() == null));
    }

    @Test
    void betweenLeftInclusive() {
        // given
        Employee employee = Employee.builder().name("Bob").salary(20L).build();
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.salary between 20 and 40", Employee.class).getResultList();
        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void betweenRightInclusive() {
        // given
        Employee employee = Employee.builder().name("Bob").salary(20L).build();
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.salary between 10 and 20", Employee.class).getResultList();
        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void betweenSalaryOutOfRange() {
        // given
        Employee employee = Employee.builder().name("Bob").salary(20L).build();
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.salary between 10 and 19", Employee.class).getResultList();
        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void likeExpressionUnderScore() {
        // given
        Employee employee = Employee.builder().name("Bob").salary(20L).build();
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.name like '_ob'", Employee.class).getResultList();
        // then
        assertFalse(result.isEmpty());
        assertEquals(employee.getName(), result.get(0).getName());
    }

    @Test
    void likeExpressionPercent() {
        // given
        Employee employee = Employee.builder().name("Bob").salary(20L).build();
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.name like 'B%'", Employee.class).getResultList();
        // then
        assertFalse(result.isEmpty());
        assertEquals(employee.getName(), result.get(0).getName());
    }

    @Test
    void likeExpressionEscapeCharacter() {
        // given
        Employee employee = Employee.builder().name("Bob_25").salary(20L).build();
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.name like 'Bob$_%' escape '$'", Employee.class).getResultList();
        // then
        assertFalse(result.isEmpty());
        assertEquals(employee.getName(), result.get(0).getName());
    }

    @Test
    void exists() {
        // given
        VirtualCompany company = VirtualCompany.getInstance(getEntityManagerFactory().createEntityManager());
        company.populate();
        // when
        List<Employee> managers = getEntityManager()
                .createQuery(
                        "select e from Employee e where exists(" +
                                "select 1 from Phone p where p.employee = e and p.type = ru.demo.jpa.entities.elementcollection.PhoneType.WORK)",
                        Employee.class).getResultList();
        // then
        assertFalse(managers.isEmpty());
        assertTrue(managers.stream()
                           .allMatch(e -> e.getPhones().stream().map(Phone::getType).anyMatch(PhoneType.WORK::equals)));


    }

    @Test
    void joinOneToManyExpectDuplications() {
        // given
        Employee employee = Employee.builder().name("Bob").build();
        Phone phone1 = Phone.builder().number("1").type(PhoneType.HOME).employee(employee).build();
        Phone phone2 = Phone.builder().number("2").type(PhoneType.WORK).employee(employee).build();
        employee.getPhones().addAll(Set.of(phone1, phone2));
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e, Phone p where e.name = 'Bob'", Employee.class).getResultList();
        // then
        assertEquals(2, result.size());
    }

    @Test
    void joinOneToManyDistinctExpectUnique() {
        // given
        Employee employee = Employee.builder().name("Bob").build();
        Phone phone1 = Phone.builder().number("1").type(PhoneType.HOME).employee(employee).build();
        Phone phone2 = Phone.builder().number("2").type(PhoneType.WORK).employee(employee).build();
        employee.getPhones().addAll(Set.of(phone1, phone2));
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select distinct e from Employee e, Phone p where e.name = 'Bob'", Employee.class).getResultList();
        // then
        assertEquals(1, result.size());
    }

    @Test
    void inExpression() {
        // given
        Employee employee = Employee.builder().name("Bob").build();
        Phone phone1 = Phone.builder().number("1").type(PhoneType.HOME).employee(employee).build();
        Phone phone2 = Phone.builder().number("2").type(PhoneType.WORK).employee(employee).build();
        employee.getPhones().addAll(Set.of(phone1, phone2));
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select distinct e from Employee e where exists(" +
                                     "select p from e.phones p where p.type in (ru.demo.jpa.entities.elementcollection.PhoneType.WORK," +
                                     " ru.demo.jpa.entities.elementcollection.PhoneType.HOME))", Employee.class).getResultList();
        // then
        assertEquals(1, result.size());
    }

    @Test
    void notInExpression() {
        // given
        Employee employee = Employee.builder().name("Bob").build();
        Phone phone1 = Phone.builder().number("1").type(PhoneType.HOME).employee(employee).build();
        Phone phone2 = Phone.builder().number("2").type(PhoneType.WORK).employee(employee).build();
        employee.getPhones().addAll(Set.of(phone1, phone2));
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select distinct e from Employee e where exists(" +
                                     "select p from e.phones p where p.type not in (ru.demo.jpa.entities.elementcollection.PhoneType.WORK," +
                                     " ru.demo.jpa.entities.elementcollection.PhoneType.HOME))", Employee.class).getResultList();
        // then
        assertEquals(0, result.size());
    }

    @Test
    void isEmpty() {
        // given
        Employee employee = Employee.builder().name("Bob").build();
        Phone phone1 = Phone.builder().number("1").type(PhoneType.HOME).employee(employee).build();
        Phone phone2 = Phone.builder().number("2").type(PhoneType.WORK).employee(employee).build();
        employee.getPhones().addAll(Set.of(phone1, phone2));
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.phones is empty", Employee.class).getResultList();
        // then
        assertEquals(0, result.size());
    }

    @Test
    void isNotEmpty() {
        // given
        Employee employee = Employee.builder().name("Bob").build();
        Phone phone1 = Phone.builder().number("1").type(PhoneType.HOME).employee(employee).build();
        Phone phone2 = Phone.builder().number("2").type(PhoneType.WORK).employee(employee).build();
        employee.getPhones().addAll(Set.of(phone1, phone2));
        getEntityManager().persist(employee);
        getEntityManager().getTransaction().commit();
        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where e.phones is not empty", Employee.class).getResultList();
        // then
        assertEquals(1, result.size());
    }

    @Test
    void memberOf() {
        // given
        Employee employee2 = Employee.builder().name("Drake").build();
        Employee employee = Employee.builder().name("Bob").build();

        Project projectX = Project.builder().name("X").build();
        Project projectAlpha = Project.builder().name("Alpha").build();
        Project projectBetta = Project.builder().name("Betta").build();

        projectX.getEmployee().add(employee);
        projectAlpha.getEmployee().add(employee);
        projectBetta.getEmployee().add(employee2);

        employee2.getProjects().add(projectBetta);
        employee.getProjects().addAll(Set.of(projectX, projectAlpha));
        getEntityManager().persist(employee);
        getEntityManager().persist(employee2);
        getEntityManager().getTransaction().commit();

        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where :project member of e.projects", Employee.class)
                .setParameter("project", projectX)
                .getResultList();
        // then
        assertEquals(1, result.size());
        assertEquals(employee, result.get(0));
    }

    @Test
    void notMemberOf() {
        // given
        Employee employee2 = Employee.builder().name("Drake").build();
        Employee employee = Employee.builder().name("Bob").build();

        Project projectX = Project.builder().name("X").build();
        Project projectAlpha = Project.builder().name("Alpha").build();
        Project projectBetta = Project.builder().name("Betta").build();

        projectX.getEmployee().add(employee);
        projectAlpha.getEmployee().add(employee);
        projectBetta.getEmployee().add(employee2);

        employee2.getProjects().add(projectBetta);
        employee.getProjects().addAll(Set.of(projectX, projectAlpha));
        getEntityManager().persist(employee);
        getEntityManager().persist(employee2);
        getEntityManager().getTransaction().commit();

        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e where :project not member of e.projects", Employee.class)
                .setParameter("project", projectX)
                .getResultList();
        // then
        assertEquals(1, result.size());
        assertEquals(employee2, result.get(0));
    }

    /**
     * Try to break unique constraint for the one to one relationship
     */
    @Test
    void oneToOneUniqueConstraintViolation() {
        // given
        Passport passport = Passport.builder().number("6996").build();

        Employee employeeDrake = Employee.builder().passport(passport).name("Drake").build();
        Employee employeeMarshall = Employee.builder().passport(passport).name("Marshall").build(); // try to steal a passport

        getEntityManager().persist(employeeDrake);
        getEntityManager().persist(employeeMarshall);

        // when - then
        assertThrows(RollbackException.class, () -> getEntityManager().getTransaction().commit());
    }

    /**
     * Relationship owner is responsible for updating the join table.
     */
    @Test
    void many_to_many_employee_is_not_relationship_owner() {
        // given
        Employee employeeDrake = Employee.builder().name("Drake").build();
        Employee employeeMarshall = Employee.builder().name("Marshall").build();

        Project projectX = Project.builder().name("X").build();

        employeeDrake.getProjects().add(projectX);

        getEntityManager().persist(employeeDrake);
        getEntityManager().getTransaction().commit();

        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e join e.projects p", Employee.class)
                .getResultList();
        // then
        assertEquals(0, result.size());
    }

    @Test
    void many_to_many_project_is_relationship_owner() {
        // given
        Employee employeeDrake = Employee.builder().name("Drake").build();
        Employee employeeMarshall = Employee.builder().name("Marshall").build();

        Project projectX = Project.builder().name("X").build();

        projectX.getEmployee().add(employeeDrake);

        getEntityManager().persist(projectX);
        getEntityManager().getTransaction().commit();

        // when
        List<Employee> result = getEntityManager()
                .createQuery("select e from Employee e join e.projects p", Employee.class)
                .getResultList();
        // then
        assertEquals(1, result.size());
    }

    @Test
    void many_to_many_owner_is_able_to_change_relation_ship() {
        // given
        Employee employeeDrake = Employee.builder().name("Drake").build();
        Employee employeeMarshall = Employee.builder().name("Marshall").build();

        Project projectX = Project.builder().name("X").build();

        projectX.getEmployee().add(employeeDrake);

        getEntityManager().persist(projectX);
        getEntityManager().persist(employeeMarshall);
        getEntityManager().getTransaction().commit();
        getEntityManager().getTransaction().begin();

        projectX.getEmployee().clear();
        projectX.getEmployee().add(employeeMarshall);
        getEntityManager().getTransaction().commit();
        getEntityManager().clear(); // employeeMarshall which in a PC does not have a projectX
        getEntityManager().getTransaction().begin();

        // when
        List<Employee> employees = getEntityManager()
                .createQuery("select e from Employee e left join fetch e.projects", Employee.class)
                .getResultList();
        // then
        assertEquals(2, employees.size());
        assertTrue(employees.removeIf(e -> e.equals(employeeMarshall) && e.getProjects().size() == 1));
        assertTrue(employees.removeIf(e -> e.equals(employeeDrake) && e.getProjects().size() == 0));
    }

    @Test
    void merge() {
        // given
        Employee employeeDrake = Employee.builder().id(3L).country("US").name("Drake").build();
        getEntityManager().persist(employeeDrake);
        commit();
        getEntityManager().clear();
        begin();
        // when
        Employee employee = getEntityManager().merge(employeeDrake);

        // then
        assertFalse(employee == employeeDrake);
        assertTrue(employee.equals(employeeDrake));
    }

    @Test
    void lazyFetchInTransactionPersistentSet() {
        // given
        Phone phone = Phone.builder().type(PhoneType.HOME).number("3232").build();
        Phone phone2 = Phone.builder().type(PhoneType.HOME).number("3232").build();
        Phone phone3 = Phone.builder().type(PhoneType.HOME).number("3232").build();
        Employee employeeDrake = Employee.builder().id(3L).country("US").name("Drake").build();

        phone.setEmployee(employeeDrake);
        phone2.setEmployee(employeeDrake);
        phone3.setEmployee(employeeDrake);
        employeeDrake.getPhones().add(phone);
        employeeDrake.getPhones().add(phone2);
        employeeDrake.getPhones().add(phone3);

        getEntityManager().persist(employeeDrake);
        commit();
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        // when
        Employee employee = em.find(Employee.class, new EmployeeId(employeeDrake.getId(), employeeDrake.getCountry()));
        assertEquals(employee.getPhones().getClass(), PersistentSet.class);
    }

    @Test
    void eagerFetch() {
        // given
        Project project = Project.builder().name("#").build();
        Employee employeeDrake = Employee.builder().id(3L).country("US").name("Drake").build();

        project.getEmployee().add(employeeDrake);
        employeeDrake.getProjects().add(project);

        getEntityManager().persist(employeeDrake);
        commit();
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        // when
        Employee employee = em.find(Employee.class, new EmployeeId(employeeDrake.getId(), employeeDrake.getCountry()));
        assertEquals(employee.getProjects().getClass(), PersistentSet.class);
    }

    @Test
    void lazyFetchWithoutTransactionPersistentSet() {
        // given
        Phone phone = Phone.builder().type(PhoneType.HOME).number("3232").build();
        Phone phone2 = Phone.builder().type(PhoneType.HOME).number("3232").build();
        Phone phone3 = Phone.builder().type(PhoneType.HOME).number("3232").build();
        Employee employeeDrake = Employee.builder().id(3L).country("US").name("Drake").build();

        phone.setEmployee(employeeDrake);
        phone2.setEmployee(employeeDrake);
        phone3.setEmployee(employeeDrake);
        employeeDrake.getPhones().add(phone);
        employeeDrake.getPhones().add(phone2);
        employeeDrake.getPhones().add(phone3);

        getEntityManager().persist(employeeDrake);
        commit();
        EntityManager em = getEntityManagerFactory().createEntityManager();
        // when
        Employee employee = em.find(Employee.class, new EmployeeId(employeeDrake.getId(), employeeDrake.getCountry()));
        assertEquals(employee.getPhones().getClass(), PersistentSet.class);
    }

    private Account buildAccount(String currency) {
        return Account.builder().serialNumber("sn - " + Instant.now()).currency(currency).openDate(LocalDateTime.now())
                      .build();
    }
}
