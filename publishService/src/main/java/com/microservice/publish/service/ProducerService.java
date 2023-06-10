package com.microservice.publish.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import com.microservice.publish.model.ImageDetail;

@Service
public final class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC = "kafkaTopic";

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		
        this.kafkaTemplate = kafkaTemplate;
    }
   
    public void sendMessage(ImageDetail imgDetail) {
       logger.info(String.format("$$$$ => Producing message: %s", imgDetail.getUserName()));
	   
	   Message<ImageDetail> message = MessageBuilder
                .withPayload(imgDetail)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
          ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
	
       future.addCallback(new ListenableFutureCallback<>() {
      
	  @Override
      public void onFailure(Throwable ex) {
       logger.info("Unable to send message=[ {} ] due to : {}", message, ex.getMessage());
      }

       @Override
      public void onSuccess(SendResult<String, String> result) {
         logger.info("Sent message=[ {} ] with offset=[ {} ]", message, result.getRecordMetadata().offset());
       }
      });
   }
}