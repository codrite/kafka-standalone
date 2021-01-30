package com.codrite.kafkastandalone;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

public class StandalonePublisher {

    KafkaProducer<String, String> kafkaProducer;

    public StandalonePublisher() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    public void start() throws InterruptedException {
        long start = System.currentTimeMillis();

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo", "key", "value");
        for(int i = 0; i < 10; i++) {
            kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();
        System.out.println("=============================================");
        System.out.println("Time to publish (ms) - " + (System.currentTimeMillis()-start));
        System.out.println("=============================================");
        System.out.println("Pausing for 2 seconds to display this message");
        Thread.sleep(2000);
    }

}
