package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import ru.demo.soccer.dto.TeamDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TeamRequestHandler extends BaseListRequestHandler<TeamDto> {

    private final ObjectMapper objectMapper;

    public TeamRequestHandler(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<TeamDto> processEntities(JsonNode apiNode, int results) throws ClientProtocolException, IOException {
        JsonNode teamsNodeArr = apiNode.get("teams");

        if (!teamsNodeArr.isArray()) {
            log.error("#processEntities: not an array");
            throw new IllegalStateException("");
        }

        List<TeamDto> result = new ArrayList<>();

        for (int i = 0; i < teamsNodeArr.size(); i++) {

            JsonNode teamNode = teamsNodeArr.get(i);

            if (teamNode == null || teamNode.isNull()) {
                log.error("#processEntites: team index #{} null", i);
                continue;
            }

            TeamDto team = objectMapper.treeToValue(teamNode, TeamDto.class);
            result.add(team);
        }
        return result;
    }
}
