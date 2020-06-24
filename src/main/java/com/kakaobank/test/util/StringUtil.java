package com.kakaobank.test.util;

import com.google.gson.Gson;
import org.apache.kudu.Hash;

import java.util.ArrayList;
import java.util.HashMap;

public class StringUtil {

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

}
