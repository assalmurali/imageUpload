
package com.microservice.publish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice.publish.models.ImageDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class EventController {
 private final ProducerService producerService;

	public EventController(ProducerService producerService) {
		// TODO Auto-generated constructor 
		 this.producerService = producerService;
	}

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestBody ImageDetail imgDetail) {
         imgDetail.sendstatus("published");
		 producerService.sendMessage(imgDetail);
		
    }
}


