package ru.demo.soccer.entities.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.demo.soccer.dto.StandingDto;
import ru.demo.soccer.entities.Standing;

@Mapper
public interface StandingDtoToStandingMapper {

    StandingDtoToStandingMapper INSTANCE = Mappers.getMapper(StandingDtoToStandingMapper.class);

    Standing standingDtoToStanding(StandingDto standingDto);

}
