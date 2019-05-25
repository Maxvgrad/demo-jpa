package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StandingDto {

    private Integer rank;

    @JsonProperty("team_id")
    private Long teamId;

    private String teamName;

    private String logo;

    private String group;

    private String forme;

    private String description;

    private All all;

    private Home home;

    private Away away;

    private Integer goalsDiff;

    private Integer points;

    private Date lastUpdate;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Away {
        private Integer matchsPlayed;
        private Integer win;
        private Integer draw;
        private Integer lose;
        private Integer goalsFor;
        private Integer goalsAgainst;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Home {
        private Integer matchsPlayed;
        private Integer win;
        private Integer draw;
        private Integer lose;
        private Integer goalsFor;
        private Integer goalsAgainst;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class All {
        private Integer matchsPlayed;
        private Integer win;
        private Integer draw;
        private Integer lose;
        private Integer goalsFor;
        private Integer goalsAgainst;

    }
}