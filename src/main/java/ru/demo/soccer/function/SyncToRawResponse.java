package ru.demo.soccer.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpRequestBase;
import ru.demo.soccer.entities.RawResponse;
import ru.demo.soccer.entities.Sync;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class SyncToRawResponse implements Function<Sync, RawResponse> {

    private final Function<Sync, HttpRequestBase> syncToHttpRequest;
    private final Function<HttpRequestBase, RawResponse> httpRequestToRawResponse;

    @Override
    public RawResponse apply(Sync sync) {
        return syncToHttpRequest.andThen(httpRequestToRawResponse).apply(sync);
    }
}
