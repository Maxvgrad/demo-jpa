package ru.demo.jpa.entities.inheritance.st;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForeignOrganisation extends Organisation {

    @Builder
    public ForeignOrganisation(Long id, String inn, String nza) {
        super(id, inn);
        this.nza = nza;
    }

    private String nza;

}
