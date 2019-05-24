package ru.demo.soccer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.demo.jpa.util.EntityManagerFactoryHolder;
import ru.demo.soccer.entities.League;
import ru.demo.soccer.handlers.LeagueHandler;
import ru.demo.soccer.service.LeagueService;

import javax.persistence.EntityManager;
import java.util.List;

public class Application {

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://api-football-v1.p.rapidapi.com/leagues");

        request.setHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
        request.setHeader("X-RapidAPI-Key", "142253f752msh23483bbd79eaf76p1876cbjsn72248d3552ba");

        ResponseHandler<List<League>> handler = new LeagueHandler(new ObjectMapper());

        List<League> leagues = client.execute(request, handler);

        EntityManagerFactoryHolder entityManagerFactoryHolder = EntityManagerFactoryHolder.getInstance("soccer");
        EntityManager entityManager = entityManagerFactoryHolder.createEntityManager();

        entityManager.getTransaction().begin();

        LeagueService service = new LeagueService(entityManager);

        service.saveAll(leagues);

        entityManager.getTransaction().commit();

        entityManagerFactoryHolder.close();
    }
}
