package ru.demo.jpa.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.demo.jpa.entities.elementcollection.PhoneType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class Phone {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "phone_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String number;

    @Enumerated(EnumType.STRING)
    private PhoneType type;

    @ManyToOne()
    private Employee employee;

}
