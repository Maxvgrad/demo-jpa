package ru.demo.soccer.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class Scheduler<T, R> implements Runnable {

    private final Supplier<T> syncSupplier;
    private final Function<T, R> dataFunction;
    private final Consumer<R> dataConsumer;
    private final Consumer<T> syncConsumer;

    @Override
    public void run() {
        log.info("#run: start");

        T sync = syncSupplier.get();

        R data = dataFunction.apply(sync);

        dataConsumer.accept(data);

        syncConsumer.accept(sync);
        log.info("#run: done");

    }
}
