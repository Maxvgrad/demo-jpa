package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import ru.demo.soccer.entities.League;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LeagueHandler implements ResponseHandler<List<League>> {

    private final ObjectMapper objectMapper;

    @Override
    public List<League> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            log.error("#handleResponse: code: {}", statusLine.getStatusCode());
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }

        JsonNode rootNode = objectMapper.readTree(entity.getContent());

        if (rootNode.isNull()) {
            log.error("#handleResponse: empty root node");
            throw new IllegalStateException();
        }

        JsonNode apiNode = rootNode.get("api");

        if (apiNode.isNull()) {
            log.error("#handleResponse: empty api node");
            throw new IllegalStateException();
        }

        JsonNode resultsNode = apiNode.get("results");
        int results;

        if (resultsNode.isNull() || (results = resultsNode.intValue()) <= 0) {
            log.error("#handleResponse: empty results");
            throw new IllegalStateException("Results null");
        }

        JsonNode leaguesNode = apiNode.get("leagues");

        List<League> result = new ArrayList<>();

        for (int i = 1; i < results; i++) {

            JsonNode leagueNode = leaguesNode.findValue("" + i);

            if (leagueNode == null || leagueNode.isNull()) {
                log.error("#handleResponse: league #{} null", i);
                continue;
            }

            League league = objectMapper.treeToValue(leagueNode, League.class);
            result.add(league);
        }

        return result;
    }
}
