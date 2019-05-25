package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerFilter {

    @JsonProperty("player_name")
    private String playerName;

    @JsonProperty("team_name")
    private String teamName;

    private Integer number;

    private Integer age;

    private Boolean injured;

    private String position;

    private String captain;

}
