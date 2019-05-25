package ru.demo.soccer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamDto {

    @JsonProperty("team_id")
    private Long teamId;

    private String name;

    private String code;

    private String logo;

    private String country;

    private Integer founded;

    @JsonProperty("venue_name")
    private String venueName;

    @JsonProperty("venue_surface")
    private String venueSurface;

    @JsonProperty("venue_address")
    private String venueAddress;

    @JsonProperty("venue_city")
    private String venueCity;

    @JsonProperty("venue_capacity")
    private Long venueCapacity;

}
