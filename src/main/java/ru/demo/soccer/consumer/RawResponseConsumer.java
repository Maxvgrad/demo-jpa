package ru.demo.soccer.consumer;

import lombok.RequiredArgsConstructor;
import ru.demo.soccer.dao.GenericDao;
import ru.demo.soccer.entities.RawResponse;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class RawResponseConsumer implements Consumer<RawResponse> {

    private final GenericDao<RawResponse, String> rawResponseRepository;

    @Override
    public void accept(RawResponse rawResponse) {
        rawResponseRepository.findById(rawResponse.getUrl()).map(r -> {
            if (rawResponse.getCode() < r.getCode()) {
                r.setCode(rawResponse.getCode());
                r.setBody(rawResponse.getBody());
                rawResponseRepository.update(r);
            }
            return r;
        }).orElseGet(() -> {
            rawResponseRepository.create(rawResponse);
            return rawResponse;
        });
    }
}
