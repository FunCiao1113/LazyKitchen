package com.example.lazykitchen.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    //JSON字符串转对象数组
    public static <T> List<T> getResultList(String data, Class<T> classtype) {
        if (data==null||data.length()==0) return null;
        List<T> members = null;
        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(data).getAsJsonArray();
            members = new ArrayList<>();
            for (JsonElement obj : Jarray) {
                T member = gson.fromJson(obj, classtype);
                members.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }
}
