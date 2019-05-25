package ru.demo.soccer.entities.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.demo.soccer.dto.StandingDto;
import ru.demo.soccer.entities.Standing;

import static org.assertj.core.api.Assertions.assertThat;

class StandingDtoToStandingMapperTest {

    private StandingDtoToStandingMapper mapper = StandingDtoToStandingMapper.INSTANCE;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String path = "json/standing/standing.json";

    @Test
    void standingDtoToStandingStat() throws Exception {
        // given
        StandingDto standingDto = objectMapper
                .readValue(ClassLoader.getSystemResourceAsStream(path), StandingDto.class);
        // when
        Standing standing = mapper.standingDtoToStanding(standingDto);
        //then
        assertThat(standing).isNotNull();
        assertThat(standing.getForme()).isEqualTo(standingDto.getForme());
        assertThat(standing.getDescription()).isEqualTo(standingDto.getDescription());
        assertThat(standing.getGroup()).isEqualTo(standingDto.getGroup());
        assertThat(standing.getLastUpdate()).isEqualTo(standingDto.getLastUpdate());
    }
}