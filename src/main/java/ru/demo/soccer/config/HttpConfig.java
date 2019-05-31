package ru.demo.soccer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demo.soccer.dto.TeamDto;
import ru.demo.soccer.handlers.BaseArrayResponseHandler;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Configuration
public class HttpConfig {

    @Bean
    public HttpClient httpClient() {

        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemResourceAsStream("soccer.properties"));

            return HttpClients.custom()
                              .setDefaultHeaders(
                                      List.of(new BasicHeader("X-RapidAPI-Host", properties.getProperty("host")),
                                              new BasicHeader("X-RapidAPI-Key", properties.getProperty("key"))))
                              .build();


        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Bean
    public ResponseHandler<List<TeamDto>> responseHandler() {
        return new BaseArrayResponseHandler<>("teams", TeamDto.class, new ObjectMapper());
    }

}
