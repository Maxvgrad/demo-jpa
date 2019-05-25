package ru.demo.soccer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import ru.demo.soccer.dto.TeamDto;
import ru.demo.soccer.entities.League;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.processor.TeamProcessor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Slf4j
public class TeamSyncJob extends BaseSyncJob {

    private final HttpClient client;
    private final EntityManager entityManager;
    private String url = "https://api-football-v1.p.rapidapi.com/v2/teams/league/";
    private final ResponseHandler<List<TeamDto>> handler;
    private final TeamProcessor processor;


    public TeamSyncJob(EntityManager entityManager, HttpClient client, ResponseHandler<List<TeamDto>> handler, TeamProcessor process) {
        super(entityManager, "team_job", "last_reviewed_league_id");
        this.client = client;
        this.entityManager = entityManager;
        this.handler = handler;
        this.processor = process;
    }

    public void process(Sync sync) {

        sync.setValue(sync.getValue() + 1);

        League league;

        try {
            league = entityManager.createQuery("select l from League l where l.leagueId = :id", League.class)
                                  .setParameter("id", sync.getValue())
                                  .getSingleResult();
        } catch (NoResultException ex) {
            log.warn("#process: league(id:{}) not found", sync.getValue());
            return;
        }

        HttpGet request = new HttpGet(url + league.getLeagueId());

        try {

            List<TeamDto> teams = client.execute(request, handler);

            if (CollectionUtils.isEmpty(teams)) {
                log.error("#process: teams not found");
                throw new IllegalStateException("Teams not found!");
            }

            processor.process(teams, league);

        } catch (Exception ex) {
            log.error("#process: ex: {}", ex);
            throw new IllegalStateException(ex);
        }






    }
}
