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
public class HomeLandSecurity extends Department {

    @Builder
    public HomeLandSecurity(Long id, String counry, String postCode) {
        super(id, counry);
        this.postCode = postCode;
    }

    @Column(name = "post_code")
    private String postCode;

}
