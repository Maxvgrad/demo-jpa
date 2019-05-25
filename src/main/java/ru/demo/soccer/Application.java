package ru.demo.soccer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import ru.demo.jpa.util.EntityManagerFactoryHolder;
import ru.demo.soccer.handlers.TeamRequestHandler;
import ru.demo.soccer.jobs.TeamSyncJob;
import ru.demo.soccer.processor.TeamProcessor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws Exception {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    Properties properties = new Properties();
                    properties.load(ClassLoader.getSystemResourceAsStream("soccer.properties"));

                    HttpClient client = HttpClientBuilder.create().setDefaultHeaders(
                            List.of(new BasicHeader("X-RapidAPI-Host", properties.getProperty("host")),
                                    new BasicHeader("X-RapidAPI-Key", properties.getProperty("key")))).build();

                    EntityManagerFactoryHolder entityManagerFactoryHolder = EntityManagerFactoryHolder
                            .getInstance("soccer");
                    EntityManager entityManager = entityManagerFactoryHolder.createEntityManager();

                    TeamSyncJob job = new TeamSyncJob(entityManager, client, new TeamRequestHandler(new ObjectMapper()),
                                                      new TeamProcessor(entityManager));


                    //job.execute();

                    //PlayerSyncJob job = new PlayerSyncJob(entityManager, client, new PlayerRequestHandler(new ObjectMapper()),
                    //                                      new PlayerProcessor(entityManager));


                    job.execute();

                    entityManagerFactoryHolder.close();

                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        };

        service.scheduleAtFixedRate(runnable, 0, 30, TimeUnit.MINUTES);
    }
}
