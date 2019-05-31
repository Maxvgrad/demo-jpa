package ru.demo.soccer.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.springframework.stereotype.Service;
import ru.demo.soccer.entities.RawResponse;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawResponseHandler implements ResponseHandler<RawResponse> {

    private final ObjectMapper objectMapper;

    @Override
    public RawResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();

        return RawResponse.builder()
                          .code(statusLine.getStatusCode())
                          .body(objectMapper.readTree(entity.getContent()))
                          .build();
    }

}
