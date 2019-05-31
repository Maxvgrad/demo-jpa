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
public class DomesticOrganisation extends Organisation {

    @Builder
    public DomesticOrganisation(Long id, String inn, String ogrn) {
        super(id, inn);
        this.ogrn = ogrn;
    }

    private String ogrn;
}
