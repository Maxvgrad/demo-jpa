package ru.demo.soccer.entities.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.demo.soccer.dto.StandingDto;
import ru.demo.soccer.entities.StandingStat;

@Mapper
public interface StandingDtoToStandingStatMapper {

    StandingDtoToStandingStatMapper INSTANCE = Mappers.getMapper(StandingDtoToStandingStatMapper.class);

    @Mapping(target = "homeMatchPlayed", source = "home.matchsPlayed")
    @Mapping(target = "homeWin", source = "home.win")
    @Mapping(target = "homeDraw", source = "home.draw")
    @Mapping(target = "homeLose", source = "home.lose")
    @Mapping(target = "homeGoalsFor", source = "home.goalsFor")
    @Mapping(target = "homeGoalsAgainst", source = "home.goalsAgainst")
    @Mapping(target = "awayMatchPlayed", source = "away.matchsPlayed")
    @Mapping(target = "awayWin", source = "away.win")
    @Mapping(target = "awayDraw", source = "away.draw")
    @Mapping(target = "awayLose", source = "away.lose")
    @Mapping(target = "awayGoalsFor", source = "away.goalsFor")
    @Mapping(target = "awayGoalsAgainst", source = "away.goalsAgainst")
    StandingStat standingDtoToStandingStat(StandingDto standingDto);

}
