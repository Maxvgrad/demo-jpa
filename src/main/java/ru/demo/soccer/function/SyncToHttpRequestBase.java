package ru.demo.soccer.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import ru.demo.soccer.entities.ApiLeague;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.service.SeasonService;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class SyncToHttpRequestBase implements Function<Sync, HttpRequestBase> {

    private final SeasonService seasonService;

    private String url = "https://api-football-v1.p.rapidapi.com/v2/teams/league/";

    @Override
    public HttpRequestBase apply(Sync sync) {

        ApiLeague league = seasonService.findByApiLeagueId(sync.getValue()).orElseThrow(() -> {
            log.error("#retrieveRequest: {}", sync);
            return new IllegalArgumentException("API legua not found!");
        });

        return new HttpGet(url + league.getApiId());
    }
}
