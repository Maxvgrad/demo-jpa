package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {

    @JsonProperty("player_id")
    private Long playerId;

    @JsonProperty("player_name")
    private String playerName;

    @JsonProperty("team_id")
    private Long teamId;

    @JsonProperty("team_name")
    private String teamName;

    private Integer number;

    private Short age;

    private String position;

    private Boolean injured;

    private String rating;

    private String captain;

    @JsonProperty("shots")
    private Shots shots;

    @JsonProperty("goals")
    private Goals goals;

    @JsonProperty("passes")
    private Passes passes;

    @JsonProperty("tackles")
    private Tackles tackles;

    @JsonProperty("duels")
    private Duels duels;

    @JsonProperty("dribbles")
    private Dribbles dribbles;

    @JsonProperty("fouls")
    private Fouls fouls;

    @JsonProperty("cards")
    private Cards cards;

    @JsonProperty("penalty")
    private Penalty penalty;

    @JsonProperty("games")
    private Games games;

    @JsonProperty("substitutes")
    private Substitutes substitutes;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Substitutes {
        private Integer in;
        private Integer out;
        private Integer bench;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Games {
        private Integer appearences;

        @JsonProperty("minutes_played")
        private Integer minutesPlayed;
        private Integer lineups;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Penalty {
        private Integer success;
        private Integer missed;
        private Integer saved;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Cards {
        private Integer yellow;
        private Integer yellowred;
        private Integer red;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Fouls {
        private Integer drawn;
        private Integer committed;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Dribbles {
        private Integer attempts;
        private Integer success;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Duels {
        private Integer total;
        private Integer won;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Tackles {
        private Integer total;
        private Integer blocks;
        private Integer interceptions;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Passes {
        private Integer total;
        private Integer accuracy;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Goals {
        private Integer total;
        private Integer conceded;
        private Integer assists;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Shots {
        private Integer total;
        private Integer on;
    }
}
