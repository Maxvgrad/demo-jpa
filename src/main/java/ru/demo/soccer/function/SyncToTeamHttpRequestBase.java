package ru.demo.soccer.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import ru.demo.soccer.dao.GenericDao;
import ru.demo.soccer.entities.ApiTeam;
import ru.demo.soccer.entities.Sync;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class SyncToTeamHttpRequestBase implements Function<Sync, HttpRequestBase> {

    private final GenericDao<ApiTeam, Long> teamDao;

    private String url = "https://api-football-v1.p.rapidapi.com/v2/players/team/";

    @Override
    public HttpRequestBase apply(Sync sync) {

        ApiTeam team = teamDao.findById(sync.getValue()).orElseThrow(() -> {
            log.error("#retrieveRequest: {}", sync);
            return new IllegalArgumentException("API legua not found!");
        });

        return new HttpGet(url + team.getApiId());
    }
}
