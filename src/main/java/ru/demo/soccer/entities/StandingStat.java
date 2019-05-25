package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StandingStat {

    @ManyToOne()
    private Team team;

    private Integer rank;

    private Integer points;

    // home

    @Column(name = "home_match_played")
    private Integer homeMatchPlayed;

    @Column(name = "home_win")
    private Integer homeWin;

    @Column(name = "home_draw")
    private Integer homeDraw;

    @Column(name = "home_lose")
    private Integer homeLose;

    @Column(name = "home_goals_for")
    private Integer homeGoalsFor;

    @Column(name = "home_goals_against")
    private Integer homeGoalsAgainst;

    // away

    @Column(name = "away_match_played")
    private Integer awayMatchPlayed;

    @Column(name = "away_win")
    private Integer awayWin;

    @Column(name = "away_draw")
    private Integer awayDraw;

    @Column(name = "away_lose")
    private Integer awayLose;

    @Column(name = "away_goals_for")
    private Integer awayGoalsFor;

    @Column(name = "away_goals_against")
    private Integer awayGoalsAgainst;
}
