package ru.demo.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Access(AccessType.FIELD)
@Builder
@ToString
public class Book { // class access type is field

    @Id
    @TableGenerator(name = "generator", table = "generators", pkColumnName = "gen_name", pkColumnValue = "book",
                    valueColumnName = "gen_value", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
    private Long id;

    private String title;

    @Transient
    private BigDecimal rate;

    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Access(AccessType.PROPERTY) // reassign class access type
    public String getRate() {
        return Optional.ofNullable(rate).map(BigDecimal::toEngineeringString).orElse("0");
    }

    public void setRate(String rate) {
        BigDecimal r = new BigDecimal(rate);

        if (BigDecimal.ZERO.compareTo(r) > 0) {
            r = BigDecimal.ZERO;
        }

        this.rate = r;
    }
}
