package com.kakaobank.test.consumer;

import com.kakaobank.test.producer.SimpleKafkaProducer;
import com.kakaobank.test.util.Log;
import com.kakaobank.test.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kudu.client.*;
import sun.reflect.annotation.ExceptionProxy;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.time.Duration;
import java.util.*;

public class SimpleKafkaConsumer {

    private static Log logger = Log.getInstance();
    private static int cutLen = 1024*8;

    public void consumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.162.165.39:9092,10.162.165.40:9092,10.162.165.41:9092");
        props.put("session.timeout.ms", "10000");
        props.put("group.id", "test");
        props.put("compression.type", "gzip");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");    // key deserializer
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");  // value deserializer
        props.put("schema.registry.url", "http://10.162.165.39:8081");
        KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("mobile", "banking"));
        KuduClient kuduClient =new KuduClient.KuduClientBuilder("pmda-reml-t1003.svr.toastmaker.net:7051").build();
        StringUtil su = new StringUtil();
        HashMap<String, String> consumStr;
        long start;
        long end;
        KuduSession session = null;
        while (true) {
            ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, GenericRecord> record : records) {
                String s = record.topic();
                logger.info( s +"/" +record.value().toString(), SimpleKafkaConsumer.class);
                if ("mobile".equals(s)) {
                    start = System.currentTimeMillis();
                    consumStr = su.jsonParser(record.value().toString());
                    try {
                        session = kuduClient.newSession();
                        KuduTable table = kuduClient.openTable("impala::test.mobile");

                        Insert insert = table.newInsert();
                        for (String key : consumStr.keySet()) {
                            insert.getRow().addString(key.toLowerCase(), consumStr.get(key));
                        }
                        session.apply(insert);
                        end = System.currentTimeMillis();
                        logger.info("consumer time during time : "+ (end-start),  SimpleKafkaConsumer.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (session != null) {
                                session.flush();
                                session.close();
                            }
                        } catch (KuduException e) {
                            logger.error(e.toString(), SimpleKafkaConsumer.class);
                        }
                    }
                } else if ("banking".equals(s)) {
                    start = System.currentTimeMillis();
                    consumStr = su.jsonParser(record.value().toString());
                    try {
                        session = kuduClient.newSession();
                        KuduTable table = kuduClient.openTable("impala::test.banking");
                        Insert insert = table.newInsert();
                        String logId = consumStr.get("logId");
                        for (String key : consumStr.keySet()) {
                            logger.info(key.toLowerCase()+" / " + consumStr.get(key), SimpleKafkaConsumer.class);
                            if("transactioncontent".equals(key.toLowerCase())) {
                                StringBuilder sb = new StringBuilder();
                                String transactioncontent = URLDecoder.decode(consumStr.get(key),"utf-8");
                                ArrayList<String> splitStr = su.getMaxByteString(transactioncontent, cutLen);
                                KuduSession session_detail = kuduClient.newSession();
                                KuduTable table_detail = kuduClient.openTable("impala::test.banking_transactioncontent");
                                String sublogid;
                                String delimeter = "";
                                int index = 0;
                                for (String str : splitStr) {
                                    sublogid = logId+"_"+index;
                                    Insert insert_detail = table_detail.newInsert();
                                    insert_detail.getRow().addString("sublogid", sublogid);
                                    insert_detail.getRow().addString("logid", logId);
                                    insert_detail.getRow().addString("transactioncontent", str);
                                    session_detail.apply(insert_detail);
                                    sb.append(delimeter).append(sublogid);
                                    delimeter = ",";
                                    index++;
                                }
                                logger.info("transactioncontentid / " + sb.toString(), SimpleKafkaConsumer.class);
                                insert.getRow().addString("transactioncontentid", sb.toString());
                            } else {
                                insert.getRow().addString(key.toLowerCase(), consumStr.get(key));
                            }
                        }
                        session.apply(insert);
                        end = System.currentTimeMillis();
                        logger.info("consumer time during time : "+ (end-start),  SimpleKafkaConsumer.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (session != null) {
                                session.flush();
                                session.close();
                            }
                        } catch (KuduException e) {
                            logger.error(e.toString(), SimpleKafkaConsumer.class);
                        }
                    }
                }
            }
        }
    }
}
