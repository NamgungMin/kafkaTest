package com.kakaobank.test.producer;

import com.kakaobank.test.entity.Mobile;
import com.kakaobank.test.util.Log;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.stream.IntStream;

public class SimpleKafkaProducer {

    private static Log logger = Log.getInstance();

    public static Properties init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.162.165.39:9092,10.162.165.40:9092,10.162.165.41:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("compression.type", "gzip");
        props.put("schema.registry.url", "http://10.162.165.39:8081");
        KafkaProducer producer = new KafkaProducer(props);

        return props;
    }

    public static void sendNoConfirmResult() {
        long start = System.currentTimeMillis();
        try(KafkaProducer<String, String> producer = new KafkaProducer<>(init())){
            producer.send(new ProducerRecord<String, String>("test", "Apache Kafka is a distributed streaming platform-sendNoConfirmResult()"));
        }catch (Exception e) {
            logger.error("error : " + e.toString(),SimpleKafkaProducer.class);
        }
        long end = System.currentTimeMillis();
        logger.info("sendNoConfirmResult - during time : "+ (end-start), SimpleKafkaProducer.class);
    }


    public static void sendSync() {
        long start = System.currentTimeMillis();
        try(KafkaProducer<String, Object> producer = new KafkaProducer<>(init())){
            Mobile modile = new Mobile();
            Schema.Parser parser = new Schema.Parser();
            logger.info(modile.getMOBILESCHEMA(),SimpleKafkaProducer.class);
            Schema schema = parser.parse(modile.getMOBILESCHEMA());
            GenericRecord avroRecord = new GenericData.Record(schema);

            avroRecord.put("logId", "2");
            avroRecord.put("userId", "KK123GG");
            avroRecord.put("logDt", "20180626010110");
            avroRecord.put("deviceId", "2x3fel12f99djr");
            avroRecord.put("remoteIp", "211.12.123.130");
            avroRecord.put("osType", "ANDROID");
            avroRecord.put("osVersion", "8.0.0");

            RecordMetadata meta = producer.send(new ProducerRecord<String, Object>("test", avroRecord)).get();

            logger.info("Partition: "+meta.partition()+", Offset: " + meta.offset(), SimpleKafkaProducer.class);
        }catch (Exception e) {
            logger.error("error : " + e.toString(),SimpleKafkaProducer.class);
        }
        long end = System.currentTimeMillis();
        logger.info("sendSync - during time : "+ (end-start),  SimpleKafkaProducer.class);
    }


    public static void sendAsync() {
        long start = System.currentTimeMillis();
        try(KafkaProducer<String, String> producer = new KafkaProducer<>(init())){
            producer.send(new ProducerRecord<String, String>("test", "Apache Kafka is a distributed streaming platform-sendAsync()"),new KafkaCallback());
        }catch (Exception e) {
            logger.error("error : " + e.toString(),SimpleKafkaProducer.class);
        }
        long end = System.currentTimeMillis();
        logger.info("sendAsync() - during time : "+ (end-start), SimpleKafkaProducer.class);
    }

    static class KafkaCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            // TODO Auto-generated method stub
            if(metadata != null) {
                logger.info("Partition: " + metadata.partition() + ", Offset: "+metadata.offset(), SimpleKafkaProducer.class);
            }else {
                logger.error("KafkaCallback - Exception", SimpleKafkaProducer.class);
            }
        }

    }
}
