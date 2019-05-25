package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseArrayResponseHandler<T> extends BaseApiResponseHandler<List<T>> {

    private final String dataArrNodeName;
    private final Class<T> dataClass;

    public BaseArrayResponseHandler(String dataArrNodeName, Class<T> dataClass, ObjectMapper objectMapper) {
        super(objectMapper);
        this.dataArrNodeName = dataArrNodeName;
        this.dataClass = dataClass;
    }

    @Override
    protected List<T> processEntities(JsonNode apiNode) throws ClientProtocolException, IOException {

        JsonNode resultsArr = apiNode.get(dataArrNodeName);

        if (!resultsArr.isArray()) {
            log.error("#processEntities: not an array");
            throw new IllegalStateException("Unexpected response!");
        }

        return process(resultsArr);
    }

    protected List<T> process(JsonNode resultsArr) throws IOException {

        List<T> result = new ArrayList<>();

        for (int i = 0; i < resultsArr.size(); i++) {

            JsonNode elementNode = resultsArr.get(i);

            if (elementNode == null || elementNode.isNull()) {
                log.error("#processEntites: element with index #{} null", i);
                continue;
            }

            T element = getObjectMapper().treeToValue(elementNode, dataClass);
            result.add(element);
        }

        return result;
    }

}
