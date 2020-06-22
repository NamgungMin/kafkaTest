package com.kakaobank.test.consumer;

import com.kakaobank.test.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kudu.client.Insert;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduSession;
import org.apache.kudu.client.KuduTable;
import sun.reflect.annotation.ExceptionProxy;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class SimpleKafkaConsumer {
    public void consumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.162.165.39:9092,10.162.165.40:9092,10.162.165.41:9092");
        props.put("session.timeout.ms", "10000");
        props.put("group.id", "test");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");    // key deserializer
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");  // value deserializer
        props.put("schema.registry.url", "http://10.162.165.39:8081");
        KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<String, GenericRecord>(props);
        consumer.subscribe(Arrays.asList("test"));
        KuduClient kuduClient =new KuduClient.KuduClientBuilder("pmda-reml-t1003.svr.toastmaker.net:7051").build();
        StringUtil su = new StringUtil();
        HashMap<String, String> consumStr;
        while (true) {
            ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, GenericRecord> record : records) {
                String s = record.topic();
                if ("test".equals(s)) {
                    System.out.println(record.value());
                    consumStr = su.jsonParser(record.value().toString());
                    try {
                        KuduSession session = kuduClient.newSession();
                        KuduTable table = kuduClient.openTable("impala::test.test_mobile");

                        Insert insert = table.newInsert();
                        for( String key : consumStr.keySet() ){
                            insert.getRow().addString(key.toLowerCase(), consumStr.get(key));
                        }
                        session.apply(insert);

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    throw new IllegalStateException("get message on topic " + record.topic());
                }
            }
        }
    }
}
