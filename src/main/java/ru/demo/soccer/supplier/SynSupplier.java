package ru.demo.soccer.supplier;

import lombok.RequiredArgsConstructor;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.service.JobService;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class SynSupplier implements Supplier<Sync> {

    private final JobService service;

    //TODO: Data Source
    private final String jobName;
    private final String key;

    @Override
    public Sync get() {

        Sync sync = service.findByNameAndValue(jobName, key);

        sync.setValue(sync.getValue() + 1);

        return sync;
    }
}
