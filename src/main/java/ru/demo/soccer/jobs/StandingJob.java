package ru.demo.soccer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import ru.demo.soccer.dto.StandingDto;
import ru.demo.soccer.entities.League;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.processor.StandingProcessor;
import ru.demo.soccer.service.JobService;
import ru.demo.soccer.service.LeagueService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
public class StandingJob extends BaseJob<List<StandingDto>> {

    private final StandingProcessor processor;
    private final LeagueService leagueService;
    private String url = "https://api-football-v1.p.rapidapi.com/v2/leagueTable/";

    public StandingJob(HttpClient client,
                       ResponseHandler<List<StandingDto>> handler,
                       EntityManager entityManager,
                       JobService service,
                       StandingProcessor processor,
                       LeagueService leagueService) {
        super(client, handler, entityManager, service, "standing_job", "last_reviewed_league_id");
        this.processor = processor;
        this.leagueService = leagueService;
    }

    private League league;

    @Override
    protected HttpRequestBase retrieveRequest(Sync sync) {

        Optional<League> leagueOpt = leagueService.findById(sync.getValue());

        if (leagueOpt.isEmpty()) {
            log.warn("#process: not league id {}", sync.getValue());
            return null;
        }

        league = leagueOpt.get();
        return new HttpGet(url + league.getLeagueId());
    }

    @Override
    protected void process(List<StandingDto> data) {
        processor.process(data, league);
    }
}
