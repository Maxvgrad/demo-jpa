package ru.demo.soccer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import ru.demo.jpa.util.EntityManagerFactoryHolder;
import ru.demo.soccer.entities.mapper.StandingDtoToStandingMapper;
import ru.demo.soccer.entities.mapper.StandingDtoToStandingStatMapper;
import ru.demo.soccer.handlers.StandingResponseHandler;
import ru.demo.soccer.jobs.StandingJob;
import ru.demo.soccer.processor.StandingProcessor;
import ru.demo.soccer.service.JobService;
import ru.demo.soccer.service.LeagueService;
import ru.demo.soccer.service.StandingService;
import ru.demo.soccer.service.TeamService;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws Exception {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("soccer.properties"));

        HttpClient client = HttpClientBuilder.create().setDefaultHeaders(
                List.of(new BasicHeader("X-RapidAPI-Host", properties.getProperty("host")),
                        new BasicHeader("X-RapidAPI-Key", properties.getProperty("key")))).build();

        EntityManagerFactoryHolder entityManagerFactoryHolder = EntityManagerFactoryHolder
                .getInstance("soccer");
        EntityManager entityManager = entityManagerFactoryHolder.createEntityManager();

        JobService jobService = new JobService(entityManagerFactoryHolder.createEntityManager());
        StandingService standingService = new StandingService(entityManagerFactoryHolder.createEntityManager());
        LeagueService leagueService = new LeagueService(entityManagerFactoryHolder.createEntityManager());
        TeamService teamService = new TeamService(entityManagerFactoryHolder.createEntityManager());
        StandingDtoToStandingStatMapper standingStatMapper = StandingDtoToStandingStatMapper.INSTANCE;
        StandingDtoToStandingMapper standingMapper = StandingDtoToStandingMapper.INSTANCE;

        StandingJob job = new StandingJob(client, new StandingResponseHandler(new ObjectMapper()),
                                          entityManager, jobService,
                                          new StandingProcessor(entityManager, standingStatMapper, standingMapper,
                                                                standingService, teamService), leagueService);

        service.scheduleAtFixedRate(job, 0, 1, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.out.println("SHUT_DOWN!!!");

            try {
                ((CloseableHttpClient) client).close();
            } catch (IOException ex) {

            }

            entityManagerFactoryHolder.close();
            service.shutdownNow();
        }));
    }
}
