package com.javarush.stepanov.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PublisherApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PublisherApplication.class, args);
    }

}
