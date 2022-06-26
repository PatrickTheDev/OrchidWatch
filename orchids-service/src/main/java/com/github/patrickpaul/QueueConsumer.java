package com.github.patrickpaul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.patrickpaul.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueConsumer {

    private final ObjectMapper mapper;

    public QueueConsumer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
        Product product = null;

        try {
            product = mapper.readValue(fileBody, Product.class);
        } catch(JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        if (product != null) System.out.println(product.toPrintString());
        // TODO: Send received orchids to db, check if already existing, if not, send to fcm-service
    }

}
