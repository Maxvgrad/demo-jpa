package ru.demo.soccer.consumer;

import lombok.RequiredArgsConstructor;
import ru.demo.soccer.dao.GenericDao;
import ru.demo.soccer.entities.Sync;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SyncConsumer implements Consumer<Sync> {

    private final GenericDao<Sync, Long> syncRepository;

    @Override
    public void accept(Sync sync) {
        syncRepository.update(sync);
    }
}
