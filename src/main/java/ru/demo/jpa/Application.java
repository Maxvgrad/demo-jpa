package ru.demo.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.jpa.apps.QueryRunnerApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class Application {

    private final String persistenceUnitName;

    public static void main(String[] args) {

        Application application = new Application("demo");
        application.execute();

    }

    private void execute() {
        log.info("#execute: persistence unit name={}", persistenceUnitName);
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {

            Runnable taskApp = new QueryRunnerApp(persistenceUnitName);

            Future<?> result = executorService.submit(taskApp);
            int i = 0;
            while (!result.isDone()) {
                TimeUnit.SECONDS.sleep(2);
                //log.info("#execute: time = {} s", i+=2);
            }
            log.info("#execute: complete");
        } catch (Exception ex) {
          log.error("#execute: ", ex);
        } finally {
            log.info("#execute: shutdown executor service");
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

}
