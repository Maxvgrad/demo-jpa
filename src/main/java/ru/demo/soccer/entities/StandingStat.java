package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.demo.soccer.entities.key.StandingStatId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = {"standing", "team"})
@ToString(of = {"standing", "team"})
@IdClass(StandingStatId.class)
public class StandingStat {

    @Id
    @ManyToOne
    private Standing standing;

    @Id
    @ManyToOne
    private Team team;

    private Integer rank;

    private Integer points;

    // home

    @Column(name = "HOME_MATCH_PLAYED")
    private Integer homeMatchPlayed;

    @Column(name = "HOME_WIN")
    private Integer homeWin;

    @Column(name = "HOME_DRAW")
    private Integer homeDraw;

    @Column(name = "HOME_LOSE")
    private Integer homeLose;

    @Column(name = "HOME_GOALS_FOR")
    private Integer homeGoalsFor;

    @Column(name = "HOME_GOALS_AGAINST")
    private Integer homeGoalsAgainst;

    // away

    @Column(name = "AWAY_MATCH_PLAYED")
    private Integer awayMatchPlayed;

    @Column(name = "AWAY_WIN")
    private Integer awayWin;

    @Column(name = "AWAY_DRAW")
    private Integer awayDraw;

    @Column(name = "AWAY_LOSE")
    private Integer awayLose;

    @Column(name = "AWAY_GOALS_FOR")
    private Integer awayGoalsFor;

    @Column(name = "AWAY_GOALS_AGAINST")
    private Integer awayGoalsAgainst;
}
