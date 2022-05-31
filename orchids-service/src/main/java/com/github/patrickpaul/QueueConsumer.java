package com.github.patrickpaul;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer {

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
        // TODO: Send received orchids to db, check if already existing, if not, send to fcm-service
        System.out.println(fileBody);
    }

}
