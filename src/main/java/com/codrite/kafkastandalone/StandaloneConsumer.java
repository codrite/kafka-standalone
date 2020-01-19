package com.codrite.kafkastandalone;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class StandaloneConsumer {

    AtomicLong count = new AtomicLong(0);
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    KafkaConsumer<String, String> kafkaConsumer;

    public StandaloneConsumer() throws InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "chiya:9092");
        properties.put("group.id", "1");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("max.poll.records", "100000");

        this.kafkaConsumer = new KafkaConsumer<>(properties);

        executorService.scheduleAtFixedRate(new Listener(), 1000, 100, TimeUnit.MILLISECONDS);
    }

    class Listener implements Runnable {
        public void run() {
            kafkaConsumer.subscribe(Collections.singleton("arnab"));
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

            System.out.println(">>> " + count.addAndGet(records.count()));

            if(count.get() == 1000000) {
                System.out.println("Completed");
            }
        }
    }

}