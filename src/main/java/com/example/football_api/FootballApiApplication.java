package com.example.football_api;


import com.example.football_api.configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class FootballApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballApiApplication.class, args);
    }

}
