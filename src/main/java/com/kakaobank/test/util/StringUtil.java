package com.kakaobank.test.util;

import com.google.gson.Gson;
import org.apache.kudu.Hash;

import java.util.HashMap;

public class StringUtil {

    public HashMap<String, String> jsonParser(String s) {
        Gson gson = new Gson();
        HashMap<String, String> result = gson.fromJson(s, HashMap.class);
        return result;
    }
}
