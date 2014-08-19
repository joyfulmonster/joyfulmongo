package com.joyfulmongo.util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static String[] toArray(JSONArray jsonArray) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add((String) jsonArray.get(i));
        }

        String[] resultArray = new String[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }
}
