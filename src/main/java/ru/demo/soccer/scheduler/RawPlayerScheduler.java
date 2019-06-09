package ru.demo.soccer.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.demo.soccer.consumer.RawResponseConsumer;
import ru.demo.soccer.consumer.SyncConsumer;
import ru.demo.soccer.dao.GenericDao;
import ru.demo.soccer.entities.ApiTeam;
import ru.demo.soccer.entities.RawResponse;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.function.HttpBaseRequestToRawResponseFunction;
import ru.demo.soccer.function.SyncToRawResponse;
import ru.demo.soccer.function.SyncToTeamHttpRequestBase;
import ru.demo.soccer.handlers.RawResponseHandler;
import ru.demo.soccer.service.JobService;
import ru.demo.soccer.supplier.SynSupplier;

import javax.annotation.PostConstruct;


@Component

public class RawPlayerScheduler {

    private final JobService jobService;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    private final GenericDao<RawResponse, String> rawResponseRepository;

    private final GenericDao<Sync, Long> syncRepository;

    private final GenericDao<ApiTeam, Long> apiTeamDao;

    private Scheduler<Sync, RawResponse> scheduler;


    public RawPlayerScheduler(JobService jobService, HttpClient client,
                              ObjectMapper objectMapper,
                              @Qualifier("rawResponseRepository") GenericDao<RawResponse, String> rawResponseRepository,
                              @Qualifier("syncRepository") GenericDao<Sync, Long> syncRepository,
                              @Qualifier("apiTeamRepository") GenericDao<ApiTeam, Long> apiTeamDao) {
        this.jobService = jobService;
        this.client = client;
        this.objectMapper = objectMapper;
        this.rawResponseRepository = rawResponseRepository;
        this.syncRepository = syncRepository;
        this.apiTeamDao = apiTeamDao;
    }

    private String jobName = "player_job";
    private String key = "last_reviewed_team_id";

    @PostConstruct
    private void init() {
        scheduler = new Scheduler<>(new SynSupplier(jobService, jobName, key),
                                    new SyncToRawResponse(new SyncToTeamHttpRequestBase(apiTeamDao),
                                                          new HttpBaseRequestToRawResponseFunction(client,
                                                                                                   new RawResponseHandler(
                                                                                                           objectMapper))),
                                    new RawResponseConsumer(rawResponseRepository),
                                    new SyncConsumer(syncRepository));
    }

    @Scheduled(fixedRate = 25 * 60_000)
    public void schedule() {
        scheduler.run();
    }
}
