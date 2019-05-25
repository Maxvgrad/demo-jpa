package ru.demo.soccer.entities.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.demo.soccer.dto.StandingDto;
import ru.demo.soccer.entities.StandingStat;

import static org.assertj.core.api.Assertions.assertThat;

class StandingDtoToStandingStatMapperTest {

    private StandingDtoToStandingStatMapper mapper = StandingDtoToStandingStatMapper.INSTANCE;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String path = "json/standing/standing.json";

    @Test
    void standingDtoToStandingStat() throws Exception {
        // given
        StandingDto standingDto = objectMapper
                .readValue(ClassLoader.getSystemResourceAsStream(path), StandingDto.class);
        // when
        StandingStat standingStat = mapper.standingDtoToStandingStat(standingDto);
        //then
        assertThat(standingStat).isNotNull();
        assertThat(standingStat.getRank()).isEqualTo(standingDto.getRank());
        assertThat(standingStat.getPoints()).isEqualTo(standingDto.getPoints());

        // away
        assertThat(standingStat.getAwayMatchPlayed()).isEqualTo(standingDto.getAway().getMatchsPlayed());
        assertThat(standingStat.getAwayWin()).isEqualTo(standingDto.getAway().getWin());
        assertThat(standingStat.getAwayDraw()).isEqualTo(standingDto.getAway().getDraw());
        assertThat(standingStat.getAwayLose()).isEqualTo(standingDto.getAway().getLose());
        assertThat(standingStat.getAwayGoalsAgainst()).isEqualTo(standingDto.getAway().getGoalsAgainst());
        assertThat(standingStat.getAwayGoalsFor()).isEqualTo(standingDto.getAway().getGoalsFor());

        // home
        assertThat(standingStat.getHomeMatchPlayed()).isEqualTo(standingDto.getHome().getMatchsPlayed());
        assertThat(standingStat.getHomeWin()).isEqualTo(standingDto.getHome().getWin());
        assertThat(standingStat.getHomeDraw()).isEqualTo(standingDto.getHome().getDraw());
        assertThat(standingStat.getHomeLose()).isEqualTo(standingDto.getHome().getLose());
        assertThat(standingStat.getHomeGoalsAgainst()).isEqualTo(standingDto.getHome().getGoalsAgainst());
        assertThat(standingStat.getHomeGoalsFor()).isEqualTo(standingDto.getHome().getGoalsFor());
    }
}