package com.github.patrickpaul;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class OrchidsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchidsServiceApplication.class, args);
    }

}
