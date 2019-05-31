package ru.demo.soccer.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import ru.demo.soccer.entities.RawResponse;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class HttpBaseRequestToRawResponseFunction implements Function<HttpRequestBase, RawResponse> {

    private final HttpClient client;
    private final ResponseHandler<RawResponse> handler;

    @Override
    public RawResponse apply(HttpRequestBase httpRequestBase) {
        try {
            RawResponse rawResponse = client.execute(httpRequestBase, handler);
            rawResponse.setUrl(httpRequestBase.getURI().toString());
            return rawResponse;
        } catch (Exception ex) {
            log.error("#apply: {}", ex.getLocalizedMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
