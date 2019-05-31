package ru.demo.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedNativeQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DELEGATION")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode()
@ToString()
@Builder
@SecondaryTables(
        {@SecondaryTable(name = "DELEGATION_PDF", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "DELEGATION_ID")})})
@NamedNativeQuery(name = "Delegation.badExampleFindAll",
                  query = "select d.id, d.serialNumber, dp.pdf from delegation d, delegation_pdf dp where d.id = ?",
                  resultClass = Delegation.class)
public class Delegation {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "DELEGATION_ID_SEQ", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String serialNumber;


    @Lob
    @Column(name = "PDF", table = "DELEGATION_PDF")
    @Basic(fetch = FetchType.LAZY)
    private byte[] pdf;

}
