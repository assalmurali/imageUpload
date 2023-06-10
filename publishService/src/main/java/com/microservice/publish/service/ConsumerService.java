package com.microservice.publish.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.microservice.publish.model.ImageDetail;

@Service
public final class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "kafkaTopic", groupId = "group_id")
    public void consume(ImageDetail message) {
        logger.info(String.format("$$$$ => Consumed User Name: %s", message.getUserName()));
		logger.info(String.format("$$$$ => Consumed UserId: %s", message.getUserId()));
		logger.info(String.format("$$$$ => Consumed Image name: %s", message.getFileName()));
		logger.info(String.format("$$$$ => Consumed Staus of publish: %s", message.getStatus()));
		//store any DB if required
    }
}