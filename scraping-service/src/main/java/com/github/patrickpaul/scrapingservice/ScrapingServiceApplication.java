package com.github.patrickpaul.scrapingservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class ScrapingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapingServiceApplication.class, args);
    }

}
