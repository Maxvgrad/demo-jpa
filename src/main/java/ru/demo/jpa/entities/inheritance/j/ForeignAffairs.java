package ru.demo.jpa.entities.inheritance.j;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ForeignAffairs extends Department {

    @Builder
    public ForeignAffairs(Long id, String counry, String zipCode) {
        super(id, counry);
        this.zipCode = zipCode;
    }

    @Column(name = "zip_code")
    private String zipCode;

}
