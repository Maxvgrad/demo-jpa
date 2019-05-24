package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class LeagueDto {

    @JsonProperty("league_id")
    private Long leagueId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("country")
    private String country;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("season")
    private String season;

    @JsonProperty("season_start")
    private Instant seasonStart;

    @JsonProperty("season_end")
    private Instant seasonEnd;

    @JsonProperty("logo")
    private String logo;

    @JsonProperty("flag")
    private String flag;

    @JsonProperty("standings")
    private Boolean standings;

    @JsonProperty("isCurrent")
    private Boolean isCurrent;

}
