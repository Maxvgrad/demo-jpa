package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import ru.demo.soccer.dto.PlayerDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlayerRequestHandler extends BaseListRequestHandler<PlayerDto> {

    private final ObjectMapper objectMapper;

    public PlayerRequestHandler(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<PlayerDto> processEntities(JsonNode apiNode, int results) throws ClientProtocolException, IOException {
        JsonNode resultsArr = apiNode.get("players");

        if (!resultsArr.isArray()) {
            log.error("#processEntities: not an array");
            throw new IllegalStateException("Unexpected response!");
        }

        List<PlayerDto> result = new ArrayList<>();

        for (int i = 0; i < resultsArr.size(); i++) {

            JsonNode elementNode = resultsArr.get(i);

            if (elementNode == null || elementNode.isNull()) {
                log.error("#processEntites: element with index #{} null", i);
                continue;
            }

            PlayerDto element = objectMapper.treeToValue(elementNode, PlayerDto.class);
            result.add(element);
        }
        return result;
    }
}
