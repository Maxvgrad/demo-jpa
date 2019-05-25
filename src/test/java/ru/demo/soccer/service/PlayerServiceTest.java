package ru.demo.soccer.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.soccer.dto.PlayerFilter;
import ru.demo.soccer.entities.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PlayerServiceTest extends BaseJpaTests {

    private PlayerService service;

    @BeforeAll
    static void setUpClass() {
        setPersistenceUnitName("soccer");
    }

    @BeforeEach
    void setUp() {
        service = new PlayerService(getEntityManager());
    }

    @Test
    void searchEmptyFilter() {
        // given
        PlayerFilter filter = PlayerFilter.builder().build();
        //when
        List<Player> players = service.search(filter);
        //then
        assertFalse(players.isEmpty());
        log.info("#searchEmptyFilter: size {}", players.size());
        players.forEach(p -> log.info("{}", p));
    }

    @Test
    void searchByTeamName() {
        // given
        PlayerFilter filter = PlayerFilter.builder().teamName("Belgium").build();
        //when
        List<Player> players = service.search(filter);
        //then
        assertFalse(players.isEmpty());
        assertTrue(players.stream().allMatch(p -> p.getTeam().getName().equals(filter.getTeamName())));
    }
}