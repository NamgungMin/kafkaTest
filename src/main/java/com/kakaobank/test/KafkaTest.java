package com.kakaobank.test;
import com.kakaobank.test.util.FileLogTail;
import com.kakaobank.test.util.Log;
import com.kakaobank.test.producer.SimpleKafkaProducer;
import com.kakaobank.test.consumer.SimpleKafkaConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaTest{

    public static void main(String[] args){
        Log logger = Log.getInstance();
        String mode = "producer";
        String type = "banking";
        int interval = 100;
        String logFilePath = "C:\\Users\\admin\\IdeaProjects\\kakao\\src\\log_data\\banking.log";

        if(args.length > 0) {
            mode = args[0];
            if("producer".equals(mode)) {
                type = args[1];
                logFilePath = args[2];
            }
        }

        logger.info("mode : " + mode + " / type : " + type,KafkaTest.class);
        if( "producer".equals(mode)) {
            // kafka producer
            SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer();
            logger.info("sendSync",KafkaTest.class);
            try {
                ExecutorService logExecutor = Executors.newFixedThreadPool(4);
                FileLogTail flt = new FileLogTail(logFilePath,interval,type);
                logExecutor.execute(flt);
            } catch (Exception e) {
                logger.error(e.toString(), KafkaTest.class);
            }
        } else if ( "consumer".equals(mode)) {
            // kafka consumer
            SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer();
            simpleKafkaConsumer.consumer();
        }

    }
}
