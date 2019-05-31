package ru.demo.soccer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import ru.demo.soccer.entities.Sync;
import ru.demo.soccer.service.JobService;

@Slf4j
public abstract class BaseJob<T> implements Runnable {

    private HttpClient client;
    private final ResponseHandler<T> handler;

    private final JobService service;

    private final String jobName;
    private final String key;


    public BaseJob(HttpClient client, ResponseHandler<T> handler,
                   JobService service, String jobName, String key) {
        this.client = client;
        this.handler = handler;
        this.service = service;
        this.jobName = jobName;
        this.key = key;
    }

    @Override
    public void run() {
        log.info("#run: ");

        Sync sync = service.findByNameAndValue(jobName, key);

        sync.setValue(sync.getValue() + 1);

        HttpRequestBase request = retrieveRequest(sync);

        if (request == null) return;

        try {
            T data = client.execute(request, handler);

            if (data == null) {
                log.error("#process: data not found");
                throw new IllegalStateException("Data not found!");
            }

            process(data);

        } catch (Exception ex) {
            log.error("#process: ex: {}", ex);
            throw new IllegalStateException(ex);
        }
    }

    protected abstract HttpRequestBase retrieveRequest(Sync sync);

    protected abstract void process(T data);

}
