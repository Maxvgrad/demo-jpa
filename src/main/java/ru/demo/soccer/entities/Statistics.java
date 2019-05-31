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

    @Column(name = "SHOTS_TOTAL")
    private Integer shotsTotal;

    @Column(name = "SHOTS_ON")
    private Integer shotsOn;

    @Column(name = "GOALS_TOTAL")
    private Integer goalsTotal;

    @Column(name = "GOALS_CONCEDED")
    private Integer goalsConceded;

    @Column(name = "GOALS_ASSISTS")
    private Integer goalsAssists;

    @Column(name = "PASSES_TOTAL")
    private Integer passesTotal;

    @Column(name = "PASSES_ACCURACY")
    private Integer passesAccuracy;

    @Column(name = "TACKLES_TOTAL")
    private Integer tacklesTotal;

    @Column(name = "TACKLES_BLOCKS")
    private Integer tacklesBlocks;

    @Column(name = "TACKLES_INTERCEPTIONS")
    private Integer tacklesInterceptions;

    @Column(name = "DUELS_TOTAL")
    private Integer duelsTotal;

    @Column(name = "DUELS_WON")
    private Integer duelsWon;

    @Column(name = "DRIBBLES_ATTEMPTS")
    private Integer dribblesAttempts;

    @Column(name = "DRIBBLES_SUCCESS")
    private Integer dribblesSuccess;

    @Column(name = "FOULS_DRAWN")
    private Integer foulsDrawn;

    @Column(name = "FOULS_COMMITTED")
    private Integer foulsCommitted;

    @Column(name = "CARDS_YELLOW")
    private Integer cardsYellow;

    @Column(name = "CARDS_YELLOWRED")
    private Integer cardsYellowred;

    @Column(name = "CARDS_RED")
    private Integer cardsRed;

    @Column(name = "PENALTY_SUCCESS")
    private Integer penaltySuccess;

    @Column(name = "PENALTY_MISSED")
    private Integer penaltyMissed;

    @Column(name = "PENALTY_SAVED")
    private Integer penaltySaved;

    @Column(name = "GAMES_APPEARENCES")
    private Integer gamesAppearences;

    @Column(name = "GAMES_MINUTES_PLAYED")
    private Integer gamesMinutesPlayed;

    @Column(name = "GAMES_LINEUPS")
    private Integer gamesLineups;

    @Column(name = "SUBSTITUTES_IN")
    private Integer substitutesIn;

    @Column(name = "SUBSTITUTES_OUT")
    private Integer substitutesOut;

    @Column(name = "SUBSTITUTES_BENCH")
    private Integer substitutesBench;

}
