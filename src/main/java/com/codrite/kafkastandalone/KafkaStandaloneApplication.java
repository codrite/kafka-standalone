package com.codrite.kafkastandalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class KafkaStandaloneApplication {

    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(KafkaStandaloneApplication.class, args);

        StandalonePublisher standalonePublisher = new StandalonePublisher();
        standalonePublisher.start();

        new StandaloneConsumer(1);
        new StandaloneConsumer(2);
        new StandaloneConsumer(3);
        new StandaloneConsumer(4);
        new StandaloneConsumer(5);
    }

}
