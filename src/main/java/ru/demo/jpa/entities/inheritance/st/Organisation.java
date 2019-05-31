package ru.demo.jpa.entities.inheritance.st;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedNativeQueries({

                            @NamedNativeQuery(name = "Organisation.findAll",
                                              query = "select dtype, id, inn, nza, ogrn from organisation",
                                              resultClass = Organisation.class)
                    })
public abstract class Organisation {

    @Id
    @SequenceGenerator(name = "seq",sequenceName = "organisation_id_seq", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String inn;

}
