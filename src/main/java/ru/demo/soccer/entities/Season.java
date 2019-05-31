package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"teams"})
@ToString(exclude = {"teams"})
public class Season {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "SEASON_ID_SEQ", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private Integer season;

    @Column(name = "SEASON_START")
    private Date seasonStart;

    @Column(name = "SEASON_END")
    private Date seasonEnd;

    @Column(name = "IS_CURRENT")
    private String current;

    private String standings;

    @ManyToOne
    private League league;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<Team> teams = new HashSet<>();
}
