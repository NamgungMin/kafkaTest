package com.kakaobank.test.util;

import com.google.gson.Gson;
import org.apache.kudu.Hash;

import java.util.ArrayList;
import java.util.HashMap;

public class StringUtil {

    public StringUtil() {
    }

    public HashMap<String, String> jsonParser(String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, HashMap.class);
    }

    public ArrayList<String> getMaxByteString(String str, int cutLen)
    {
        ArrayList<String> cutResult = new ArrayList<>();
        if( str.getBytes().length <= cutLen) {
            cutResult.add(str);
            return cutResult;
        } else {
            StringBuilder sb = new StringBuilder();
            int cutNum = 0;
            String curChar;

            for (char ch : str.toCharArray()) {
                cutNum += String.valueOf(ch).getBytes().length;
                if(cutNum > cutLen) {
                    cutResult.add(sb.toString());
                    sb.setLength(0);
                    cutNum = 0;
                } else {
                    sb.append(ch);
                }
            }
            if(sb.length() > 0) {
                cutResult.add(sb.toString());
            }
        }
        return cutResult;
    }

    private String[] mobileColumns = {"logId", "userId","logDt","deviceId","remoteIp","osType","osVersion"};
    private String[] bankingColumns = {"transactionDt", "logId", "logDt", "resultCode", "userId", "transactionContent"};
    public HashMap<String, String> parseLog(String log, String type) throws Exception {
        HashMap<String, String> resultMap = new HashMap<>();
        String temp[] = log.split(" ");
        int index=0;
        Exception e;
        if("mobile".equals(type)) {
            if(mobileColumns.length != temp.length) {
                e = new Exception("Wrong mobile log : " + log);
                throw e;
            }
            for(String key:mobileColumns) {
                resultMap.put(key, temp[index]);
                index++;
            }
        } else if("banking".equals(type)) {
            if(bankingColumns.length > temp.length) {
                e = new Exception("Wrong banking log : " + log);
                throw e;
            }
            for(String key:bankingColumns) {
                if("transactionContent".equals(key)) {
                    StringBuilder sb = new StringBuilder();
                    String delimeter ="";
                    for (int i = index;i<temp.length;i++) {
                        sb.append(delimeter).append(temp[i]);
                        delimeter=" ";
                    }
                    resultMap.put(key, sb.toString());
                } else {
                    resultMap.put(key, temp[index]);
                }
                index++;
            }
        }
        return resultMap;
    }

}
