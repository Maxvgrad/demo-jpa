package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venue {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "VENUE_ID_NAME", allocationSize = 1, initialValue = 10_000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Column(name = "VENUE_NAME")
    private String venueName;

    @Column(name = "VENUE_SURFACE")
    private String venueSurface;

    @Column(name = "VENUE_ADDRESS")
    private String venueAddress;

    @Column(name = "VENUE_CITY")
    private String venueCity;

    @Column(name = "VENUE_CAPACITY")
    private Long venueCapacity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Country country;

    @OneToMany
    @Builder.Default
    private Set<Team> teams = new HashSet<>();
}
