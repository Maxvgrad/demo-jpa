package ru.demo.soccer.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@EqualsAndHashCode
@ToString
public class League {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "league_id_seq", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    /**
     * www.api-football.com
     */
    @Column(name = "api_football_com_league_id")
    private Long leagueId;

    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Country country;

    private String logo;

    @Embedded
    private Season season;

    @OneToMany
    @Builder.Default
    private Set<Team> teams = new HashSet<>();
}
