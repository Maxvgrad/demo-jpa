package ru.demo.soccer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import ru.demo.soccer.dto.PlayerDto;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.entities.Team;
import ru.demo.soccer.handlers.PlayerRequestHandler;
import ru.demo.soccer.processor.PlayerProcessor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Slf4j
public class PlayerSyncJob extends BaseSyncJob {

    private final HttpClient client;
    private final EntityManager entityManager;
    private final ResponseHandler<List<PlayerDto>> handler;
    private final PlayerProcessor processor;

    private String url = "https://api-football-v1.p.rapidapi.com/v2/players/team/";

    public PlayerSyncJob(EntityManager entityManager, HttpClient client, PlayerRequestHandler handler,
                         PlayerProcessor processor) {
        super(entityManager, "player_job", "last_reviewed_team_id");
        this.client = client;
        this.entityManager = entityManager;
        this.handler = handler;
        this.processor = processor;
    }

    @Override
    protected void process(Sync sync) {

        sync.setValue(sync.getValue() + 1);

        Team team;
        try {
            team = entityManager.createQuery("select t from Team t where t.teamId = :id", Team.class)
                                .setParameter("id", sync.getValue()).getSingleResult();

        } catch (NoResultException ex) {
            log.warn("#process: league(id:{}) not found", sync.getValue());
            return;
        }


        HttpGet request = new HttpGet(url + team.getTeamId());

        try {
            List<PlayerDto> data = client.execute(request, handler);

            if (CollectionUtils.isEmpty(data)) {
                log.error("#process: data not found");
                throw new IllegalStateException("Data not found!");
            }

            processor.process(data, team);

        } catch (Exception ex) {
            log.error("#process: ex: {}", ex);
            throw new IllegalStateException(ex);
        }
    }

}
