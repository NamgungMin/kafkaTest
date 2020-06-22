package com.kakaobank.test;
import com.kakaobank.test.util.Log;
import com.kakaobank.test.producer.SimpleKafkaProducer;
import com.kakaobank.test.consumer.SimpleKafkaConsumer;

public class KafkaTest{

    public static void main(String[] args){
        System.out.println("start");
        Log logger = Log.getInstance();
        logger.info("test",KafkaTest.class);
        //String mode = args[0];

        // kafka producser Test
        SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer();
        logger.info("sendSync",KafkaTest.class);
        simpleKafkaProducer.sendSync();

        // kafka consumer Test
//        SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer();
//        simpleKafkaConsumer.consumer();
    }
}
