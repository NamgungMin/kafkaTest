package com.kakaobank.test.producer;

import com.kakaobank.test.entity.Banking;
import com.kakaobank.test.entity.Mobile;
import com.kakaobank.test.util.Log;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.IntStream;

public class SimpleKafkaProducer {

    private static Log logger = Log.getInstance();

    public static String convert(String str) {
        StringBuffer ostr = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if ((ch >= 0x0020) && (ch <= 0x007e)) {
                ostr.append(ch);
            } else {
                ostr.append("\\u");
                String hex = Integer.toHexString(str.charAt(i) & 0xFFFF);
                for (int j = 0; j < 4 - hex.length(); j++)
                    ostr.append("0");

                ostr.append(hex.toLowerCase());
            }
        }
        return (new String(ostr));
    }

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

    public static void sendSync(HashMap<String, String> logMap, String mode) {
        long start = System.currentTimeMillis();
        try(KafkaProducer<String, Object> producer = new KafkaProducer<>(init())){
            RecordMetadata meta;
            Schema.Parser parser = new Schema.Parser();
            logger.info("mode : " + mode, SimpleKafkaProducer.class);
            if("mobile".equals(mode)) {
                Mobile modile = new Mobile();
                logger.info(modile.getMOBILESCHEMA(), SimpleKafkaProducer.class);
                Schema schema = parser.parse(modile.getMOBILESCHEMA());
                GenericRecord avroRecord = new GenericData.Record(schema);
                for (String key : logMap.keySet()) {
                    avroRecord.put(key, logMap.get(key));
                }
                meta = producer.send(new ProducerRecord<String, Object>("mobile", avroRecord)).get();
            } else if ("banking".equals(mode)) {
                Banking banking = new Banking();
                logger.info(banking.getBANKINGSCHEMA(), SimpleKafkaProducer.class);
                Schema schema = parser.parse(banking.getBANKINGSCHEMA());
                GenericRecord avroRecord = new GenericData.Record(schema);

                for (String key : logMap.keySet()) {
                    if( "transactionContent".equals(key)){
                        avroRecord.put(key, URLEncoder.encode(logMap.get(key), "UTF-8"));
                    } else {
                        avroRecord.put(key, logMap.get(key));
                    }
                }
                meta = producer.send(new ProducerRecord<String, Object>("banking", avroRecord)).get();
            } else {
                Exception e = new Exception("Wrong mode : " + mode);
                throw e;
            }
            logger.info("Partition: " + meta.partition() + ", Offset: " + meta.offset(), SimpleKafkaProducer.class);
        }catch (Exception e) {
            logger.error("error : " + e.toString(),SimpleKafkaProducer.class);
        }
        long end = System.currentTimeMillis();
        logger.info("sendSync - during time : "+ (end-start),  SimpleKafkaProducer.class);
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
