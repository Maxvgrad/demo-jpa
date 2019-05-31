package ru.demo.soccer.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.demo.soccer.consumer.RawResponseConsumer;
import ru.demo.soccer.consumer.SyncConsumer;
import ru.demo.soccer.dao.GenericDao;
import ru.demo.soccer.entities.RawResponse;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.function.HttpBaseRequestToRawResponseFunction;
import ru.demo.soccer.function.SyncToHttpRequestBase;
import ru.demo.soccer.function.SyncToRawResponse;
import ru.demo.soccer.handlers.RawResponseHandler;
import ru.demo.soccer.service.JobService;
import ru.demo.soccer.service.SeasonService;
import ru.demo.soccer.supplier.SynSupplier;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class RawTeamScheduler {

    private final JobService jobService;
    private final SeasonService seasonService;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final GenericDao<RawResponse, String> rawResponseRepository;
    private final GenericDao<Sync, String> syncRepository;

    private Scheduler<Sync, RawResponse> scheduler;

    private String jobName = "team_job";
    private String key = "last_reviewed_league_id";

    @PostConstruct
    private void init() {
        scheduler = new Scheduler<>(new SynSupplier(jobService, jobName, key),
                                    new SyncToRawResponse(new SyncToHttpRequestBase(seasonService),
                                                          new HttpBaseRequestToRawResponseFunction(client,
                                                                                                   new RawResponseHandler(
                                                                                                           objectMapper))),
                                    new RawResponseConsumer(rawResponseRepository),
                                    new SyncConsumer(syncRepository));
    }

    @Scheduled(fixedRate = 360_000)
    public void schedule() {
        scheduler.run();
    }
}
