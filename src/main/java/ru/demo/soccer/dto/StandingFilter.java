package ru.demo.soccer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StandingFilter {

    private Long leagueId;

    private Date updateDate;

    private String groupName;

}
