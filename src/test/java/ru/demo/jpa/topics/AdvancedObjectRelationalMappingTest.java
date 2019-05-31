package ru.demo.jpa.topics;


import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.dto.DocumentType;
import ru.demo.jpa.entities.Delegation;
import ru.demo.jpa.entities.Document;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdvancedObjectRelationalMappingTest extends BaseJpaTests {


    @Test
    void converterTest() {
        // given
        Document act = Document.builder().documentType(DocumentType.ACT).build();
        Document invoice = Document.builder().documentType(DocumentType.INVOICE).build();

        // when
        getEntityManager().persist(act);
        getEntityManager().persist(invoice);
        getEntityManager().getTransaction().commit();

        // then
        Document document = getEntityManager().find(Document.class, act.getId());
        assertEquals(DocumentType.ACT, document.getDocumentType());
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
        // given
        Delegation delegation = Delegation.builder().serialNumber("54232A332DLD").pdf(new byte[]{3,4,5,63,3,2}).build();
        getEntityManager().persist(delegation);
        commit();
        getEntityManager().clear();
        // when
        Delegation fromDb = getEntityManager().find(Delegation.class, delegation.getId());
        getEntityManager().detach(fromDb);

        // then
        assertFalse(Hibernate.isPropertyInitialized(fromDb, "pdf")); // TODO
    }
}
