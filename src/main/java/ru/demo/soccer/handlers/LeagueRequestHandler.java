package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import ru.demo.soccer.dto.LeagueDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LeagueRequestHandler extends BaseListRequestHandler<LeagueDto> {

    private final ObjectMapper objectMapper;

    public LeagueRequestHandler(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<LeagueDto> processEntities(JsonNode apiNode, int results) throws ClientProtocolException, IOException {

        JsonNode leaguesNode = apiNode.get("leagues");

        List<LeagueDto> result = new ArrayList<>();

        for (int i = 1; i < results; i++) {

            JsonNode leagueNode = leaguesNode.findValue("" + i);

            if (leagueNode == null || leagueNode.isNull()) {
                log.error("#handleResponse: league #{} null", i);
                continue;
            }

            LeagueDto league = objectMapper.treeToValue(leagueNode, LeagueDto.class);

            if (!league.getLeagueId().equals("" + i)) {
                log.warn("#handleResponse: {} != {}", league.getLeagueId(), i);
            }

            result.add(league);
        }

        return result;
    }
}
