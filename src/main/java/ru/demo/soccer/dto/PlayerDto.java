package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

    private String injured;

    private String rating;

    private Integer captain;

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

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Substitutes {
    private float in;
    private float out;
    private float bench;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Games {
    private float appearences;
    private float minutes_played;
    private float lineups;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Penalty {
    private float success;
    private float missed;
    private float saved;

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Cards {
    private float yellow;
    private float yellowred;
    private float red;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Fouls {
    private float drawn;
    private float committed;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Dribbles {
    private float attempts;
    private float success;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Duels {
    private float total;
    private float won;

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Tackles {
    private float total;
    private float blocks;
    private float interceptions;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Passes {
    private float total;
    private float accuracy;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Goals {
    private float total;
    private float conceded;
    private float assists;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Shots {
    private float total;
    private float on;
}
