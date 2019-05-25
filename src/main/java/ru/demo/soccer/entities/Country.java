package ru.demo.soccer.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"leagues", "venues"})
@ToString(exclude = {"leagues", "venues"})
public class Country {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "country_id_seq", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @JsonProperty("league_id")
    private Long id;

    private String country;

    @Column(name = "country_code")
    private String countryCode;

    private String flag;

    @OneToMany(mappedBy = "country")
    @Builder.Default
    private Set<League> leagues = new HashSet<>();

    @OneToMany(mappedBy = "country")
    @Builder.Default
    private Set<Venue> venues = new HashSet<>();
}
