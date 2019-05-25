package ru.demo.soccer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.dto.PlayerDto;
import ru.demo.soccer.entities.Player;
import ru.demo.soccer.entities.Statistics;
import ru.demo.soccer.entities.Team;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PlayerProcessor {

    private final EntityManager entityManager;

    private Team team;

    /**
     * Process all teams
     */
    public void process(List<PlayerDto> playerDtos, Team team) {
        log.debug("#process: data size {}", playerDtos.size());
        this.team = team;
        playerDtos.forEach(this::processInternal);
    }

    private void processInternal(PlayerDto playerDto) {
        log.debug("#processInternal: {}", playerDto);

        PlayerDto.Cards cards = Optional.ofNullable(playerDto.getCards()).orElse(PlayerDto.Cards.builder().build());
        PlayerDto.Dribbles dribbles = Optional.ofNullable(playerDto.getDribbles()).orElse(PlayerDto.Dribbles.builder().build());
        PlayerDto.Duels duels = Optional.ofNullable(playerDto.getDuels()).orElse(PlayerDto.Duels.builder().build());
        PlayerDto.Substitutes substitutes = Optional.ofNullable(playerDto.getSubstitutes()).orElse(
                PlayerDto.Substitutes.builder().build());
        PlayerDto.Games games = Optional.ofNullable(playerDto.getGames()).orElse(PlayerDto.Games.builder().build());
        PlayerDto.Penalty penalty = Optional.ofNullable(playerDto.getPenalty()).orElse(PlayerDto.Penalty.builder().build());
        PlayerDto.Fouls fouls = Optional.ofNullable(playerDto.getFouls()).orElse(PlayerDto.Fouls.builder().build());
        PlayerDto.Tackles tackles = Optional.ofNullable(playerDto.getTackles()).orElse(PlayerDto.Tackles.builder().build());
        PlayerDto.Passes passes = Optional.ofNullable(playerDto.getPasses()).orElse(PlayerDto.Passes.builder().build());
        PlayerDto.Goals goals = Optional.ofNullable(playerDto.getGoals()).orElse(PlayerDto.Goals.builder().build());
        PlayerDto.Shots shots = Optional.ofNullable(playerDto.getShots()).orElse(PlayerDto.Shots.builder().build());

        Statistics statistics = Statistics.builder()
                                          //
                                          .cardsRed(cards.getRed())
                                          .cardsYellow(cards.getYellow())
                                          .cardsYellowred(cards.getYellowred())
                                          //
                                          .dribblesAttempts(dribbles.getAttempts())
                                          .dribblesSuccess(dribbles.getSuccess())
                                          //
                                          .duelsTotal(duels.getTotal())
                                          .duelsWon(duels.getWon())
                                          //
                                          .substitutesBench(substitutes.getBench())
                                          .substitutesIn(substitutes.getIn())
                                          .substitutesOut(substitutes.getOut())
                                          //
                                          .gamesAppearences(games.getAppearences())
                                          .gamesLineups(games.getLineups())
                                          .gamesMinutesPlayed(games.getMinutesPlayed())
                                          //
                                          .penaltyMissed(penalty.getMissed())
                                          .penaltySaved(penalty.getSaved())
                                          .penaltySuccess(penalty.getSuccess())
                                          //
                                          .foulsCommitted(fouls.getCommitted())
                                          .foulsDrawn(fouls.getDrawn())
                                          //
                                          .tacklesBlocks(tackles.getBlocks())
                                          .tacklesInterceptions(tackles.getInterceptions())
                                          .tacklesTotal(tackles.getTotal())
                                          //
                                          .passesAccuracy(passes.getAccuracy())
                                          .passesTotal(passes.getTotal())
                                          //
                                          .goalsAssists(goals.getAssists())
                                          .goalsConceded(goals.getConceded())
                                          .goalsTotal(goals.getTotal())
                                          //
                                          .shotsOn(shots.getOn())
                                          .shotsTotal(shots.getTotal())
                                          //
                                          .build();


        Player player = Player.builder()
                              .age(playerDto.getAge())
                              .captain(playerDto.getCaptain())
                              .injured(playerDto.getInjured())
                              .number(playerDto.getNumber())
                              .position(playerDto.getPosition())
                              .team(team)
                              .statistics(statistics)
                              .playerId(playerDto.getPlayerId())
                              .playerName(playerDto.getPlayerName())
                              .build();

        entityManager.persist(player);
    }

}
