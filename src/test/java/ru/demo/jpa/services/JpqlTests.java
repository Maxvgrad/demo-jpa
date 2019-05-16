package ru.demo.jpa.services;

import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.dtos.SerialNumCurrencyHolder;
import ru.demo.jpa.entities.Account;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private Account buildAccount(String currency) {
        return Account.builder().serialNumber("sn - " + Instant.now()).currency(currency).openDate(LocalDateTime.now())
                      .build();
    }
}
