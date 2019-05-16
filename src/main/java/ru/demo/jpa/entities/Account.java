package ru.demo.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@NamedQueries({
        @NamedQuery(name = "Account.findByCurrency", query = "select a from Account a where a.currency = :currency"),
        @NamedQuery(name = "Account.countAll", query = "select count(1) from Account a")
              })
public class Account { // class with a field access type

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "account_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String serialNumber;

    private String currency;

    private LocalDateTime openDate;

    private Long balance;
}
