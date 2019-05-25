package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.dto.StandingDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StandingResponseHandler extends BaseArrayResponseHandler<StandingDto> {

    public StandingResponseHandler(ObjectMapper objectMapper) {
        super("standings", StandingDto.class, objectMapper);
    }

    @Override
    protected List<StandingDto> process(JsonNode resultsArr) throws IOException {
        List<StandingDto> result = new ArrayList<>();

        for (int i = 0; i < resultsArr.size(); i++) {

            JsonNode innerArrNode = resultsArr.get(i);

            if (innerArrNode == null || !innerArrNode.isArray()) {
                log.error("#processEntites: element with index #{} null", i);
                continue;
            }

            for(int j = 0; j < innerArrNode.size(); j++) {

                JsonNode elementNode = innerArrNode.get(j);

                StandingDto element = getObjectMapper().treeToValue(elementNode, StandingDto.class);
                result.add(element);
            }
        }

        return result;
    }
}
