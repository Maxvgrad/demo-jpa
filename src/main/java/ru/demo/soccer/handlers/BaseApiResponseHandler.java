package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class BaseApiResponseHandler<T> implements ResponseHandler<T> {

    private final ObjectMapper objectMapper;

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
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

        return processEntities(apiNode);
    }

    protected abstract T processEntities(JsonNode apiNode) throws ClientProtocolException, IOException;
}
