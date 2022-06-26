package com.github.patrickpaul.scrapingservice.rabbitmq;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class QueueSender {

    private final RabbitTemplate template;
    private final Queue queue;

    public QueueSender(RabbitTemplate template, Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    public void send(Product orchid) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");

        template.send(
                this.queue.getName(),
                MessageBuilder
                        .withBody(orchid.toJSON().getBytes(StandardCharsets.UTF_8))
                        .andProperties(properties)
                        .build()
        );
    }

}
