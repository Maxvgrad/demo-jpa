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
    @SequenceGenerator(name = "seq", sequenceName = "venue_id_name", allocationSize = 1, initialValue = 10_000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "venue_surface")
    private String venueSurface;

    @Column(name = "venue_address")
    private String venueAddress;

    @Column(name = "venue_city")
    private String venueCity;

    @Column(name = "venue_capacity")
    private Long venueCapacity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Country country;

    @OneToMany
    @Builder.Default
    private Set<Team> teams = new HashSet<>();
}
