package com.github.patrickpaul;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

}
