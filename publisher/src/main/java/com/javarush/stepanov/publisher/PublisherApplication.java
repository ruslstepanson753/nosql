package com.javarush.stepanov.publisher;

import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.service.ProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PublisherApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PublisherApplication.class, args);
        ProducerService producerService = context.getBean(ProducerService.class);

        for (int i = 0; i < 100; i++) {
            Kafka someCustomObject = new Kafka();
            someCustomObject.setId(Long.valueOf(i));
            System.out.println("*".repeat(100));
            producerService.sendMessage(String.valueOf(i), someCustomObject);
        }
    }

}
