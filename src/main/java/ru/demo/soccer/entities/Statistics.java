package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {

    private String rating;

    @Column(name = "shots_total")
    private Integer shotsTotal;

    @Column(name = "shots_on")
    private Integer shotsOn;

    @Column(name = "goals_total")
    private Integer goalsTotal;

    @Column(name = "goals_conceded")
    private Integer goalsConceded;

    @Column(name = "goals_assists")
    private Integer goalsAssists;

    @Column(name = "passes_total")
    private Integer passesTotal;

    @Column(name = "passes_accuracy")
    private Integer passesAccuracy;

    @Column(name = "tackles_total")
    private Integer tacklesTotal;

    @Column(name = "tackles_blocks")
    private Integer tacklesBlocks;

    @Column(name = "tackles_interceptions")
    private Integer tacklesInterceptions;

    @Column(name = "duels_total")
    private Integer duelsTotal;

    @Column(name = "duels_won")
    private Integer duelsWon;

    @Column(name = "dribbles_attempts")
    private Integer dribblesAttempts;

    @Column(name = "dribbles_success")
    private Integer dribblesSuccess;

    @Column(name = "fouls_drawn")
    private Integer foulsDrawn;

    @Column(name = "fouls_committed")
    private Integer foulsCommitted;

    @Column(name = "cards_yellow")
    private Integer cardsYellow;

    @Column(name = "cards_yellowred")
    private Integer cardsYellowred;

    @Column(name = "cards_red")
    private Integer cardsRed;

    @Column(name = "penalty_success")
    private Integer penaltySuccess;

    @Column(name = "penalty_missed")
    private Integer penaltyMissed;

    @Column(name = "penalty_saved")
    private Integer penaltySaved;

    @Column(name = "games_appearences")
    private Integer gamesAppearences;

    @Column(name = "games_minutes_played")
    private Integer gamesMinutesPlayed;

    @Column(name = "games_lineups")
    private Integer gamesLineups;

    @Column(name = "substitutes_in")
    private Integer substitutesIn;

    @Column(name = "substitutes_out")
    private Integer substitutesOut;

    @Column(name = "substitutes_bench")
    private Integer substitutesBench;

}
