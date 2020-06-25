package com.kakaobank.test.util;

import com.kakaobank.test.producer.SimpleKafkaProducer;
import com.kakaobank.test.util.Log;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class FileLogTail implements Runnable {

    private int FileLogTailSeconds = 2000;
    private String type;
    private long lastKnownPosition = 0;
    private boolean shouldIRun = true;
    private File FileLogTailFile = null;
    private Log logger = Log.getInstance();

    public FileLogTail(String logFilePath, int interval, String type) {
        FileLogTailFile = new File(logFilePath);
        this.FileLogTailSeconds = interval;
        this.type = type;
    }

    public void stopRunning() {
        shouldIRun = false;
    }

    public void run() {
        try {
            StringUtil su = new StringUtil();
            SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer();
            while (shouldIRun) {
                Thread.sleep(FileLogTailSeconds);
                long fileLength = FileLogTailFile.length();
                if (fileLength > lastKnownPosition) {

                    // Reading
                    RandomAccessFile readWriteFileAccess = new RandomAccessFile(FileLogTailFile, "r");
                    readWriteFileAccess.seek(lastKnownPosition);
                    String log = null;
                    while ((log = readWriteFileAccess.readLine()) != null) {
                        if( log.length() > 0) {
                            try {
                                HashMap<String, String> logMap = su.parseLog(new String(log.getBytes("iso-8859-1"), "utf-8"), type);
                                logger.info(logMap.toString(), FileLogTail.class);
                                simpleKafkaProducer.sendSync(logMap, type);
                            } catch (Exception e) {
                                logger.error(e.toString(), FileLogTail.class);
                            }
                        }
                    }
                    lastKnownPosition = readWriteFileAccess.getFilePointer();
                    readWriteFileAccess.close();
                } else {
                    Thread.sleep(10);
                }
            }
        } catch (Exception e) {
            logger.error("FileLogTail error : " + e.toString(), FileLogTail.class);
            stopRunning();
        }
    }
}
