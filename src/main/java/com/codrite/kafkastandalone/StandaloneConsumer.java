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

    private final int id;
    AtomicLong count = new AtomicLong(0);
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    KafkaConsumer<String, String> kafkaConsumer;

    public StandaloneConsumer(int id) {
        this.id = id;
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("client.id", ""+id);
        properties.put("group.id", "1");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("max.poll.records", "65000");

        this.kafkaConsumer = new KafkaConsumer<>(properties);

        executorService.scheduleAtFixedRate(new Listener(), 1000, 2000, TimeUnit.MILLISECONDS);
    }

    class Listener implements Runnable {
        public void run() {
//            if(count.get() == 10) {
//                return;
//            }
            kafkaConsumer.subscribe(Collections.singleton("demo"));
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

            System.out.println(">>> " + id + " : " + count.addAndGet(records.count()));


        }
    }

}