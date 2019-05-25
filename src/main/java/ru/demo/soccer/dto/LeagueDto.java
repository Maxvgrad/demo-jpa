package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class LeagueDto {

    @JsonProperty("league_id")
    private String leagueId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("country")
    private String country;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("season")
    private Integer season;

    @JsonProperty("season_start")
    private Date seasonStart;

    @JsonProperty("season_end")
    private Date seasonEnd;

    @JsonProperty("logo")
    private String logo;

    @JsonProperty("flag")
    private String flag;

    @JsonProperty("standings")
    private String standings;

    @JsonProperty("is_current")
    private String current;

}
