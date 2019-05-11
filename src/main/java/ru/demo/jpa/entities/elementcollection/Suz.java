package ru.demo.jpa.entities.elementcollection;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Suz {

    private String group;

    private Long number;
}
