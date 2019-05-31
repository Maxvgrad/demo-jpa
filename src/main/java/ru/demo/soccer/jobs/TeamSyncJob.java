package ru.demo.soccer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.stereotype.Service;
import ru.demo.soccer.dto.TeamDto;
import ru.demo.soccer.entities.ApiLeague;
import ru.demo.soccer.entities.Season;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.processor.TeamProcessor;
import ru.demo.soccer.service.JobService;
import ru.demo.soccer.service.SeasonService;

import java.util.List;

@Slf4j
@Service
public class TeamSyncJob extends BaseJob<List<TeamDto>> {

    private String url = "https://api-football-v1.p.rapidapi.com/v2/teams/league/";

    private final TeamProcessor processor;

    private final SeasonService seasonService;

    private Season season;

    public TeamSyncJob(HttpClient client,
                       ResponseHandler<List<TeamDto>> handler,
                       JobService service, TeamProcessor processor,
                       SeasonService seasonService) {
        super(client, handler, service, "team_job", "last_reviewed_league_id");

        this.processor = processor;
        this.seasonService = seasonService;
    }

    @Override
    protected HttpRequestBase retrieveRequest(Sync sync) {

        ApiLeague league = seasonService.findByApiLeagueId(sync.getValue()).orElseThrow(() -> {
            log.error("#retrieveRequest: {}", sync);
            return new IllegalArgumentException("API legua not found!");
        });

        this.season = league.getSeason();
        return new HttpGet(url + league.getApiId());
    }

    @Override
    protected void process(List<TeamDto> data) {
        processor.process(data, season);
    }

}
