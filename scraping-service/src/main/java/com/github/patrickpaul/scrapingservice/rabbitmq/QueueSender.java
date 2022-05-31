package com.github.patrickpaul.scrapingservice.rabbitmq;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {

    private final RabbitTemplate template;
    private final Queue queue;

    public QueueSender(RabbitTemplate template, Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    public void send(Product orchid) {
        template.convertAndSend(this.queue.getName(), orchid);
    }

}
