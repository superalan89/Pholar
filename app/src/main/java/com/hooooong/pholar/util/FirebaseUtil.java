package com.hooooong.pholar.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 9..
 * Firebase와 관련된 Util 클래스
 */

public class FirebaseUtil {
    private static final String TAG = "FirebaseUtil";

    // Firebase Object -> Json -> Java Object
    public static <U> List<U> getNestedClass(Map<String, Map<String, String>> target, Class<U> CLASS) {
        List<U> ret = new ArrayList<>();

        HashMap<String, String> hashMap = new HashMap<>();
        Gson gson = new Gson();
        JSONObject jsonObject;

        Iterator<String> ids = target.keySet().iterator();

        while(ids.hasNext()) {
            String id = ids.next();
            Map<String, String> map2 = target.get(id);
            Iterator<String> keys = map2.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                Log.e(TAG, "\t\tKey: " + key + " Value: " + map2.get(key));
                hashMap.put(key, map2.get(key));
            }

            jsonObject = getJsonStringFromMap(hashMap);
            Log.d("heepie", jsonObject.toString());
            ret.add(gson.fromJson(jsonObject.toString(), CLASS));

        }

        return ret;
    }

    // Map -> JSON 객체로 변경해주는 메소드
    public static JSONObject getJsonStringFromMap(Map<String, String> hashMap) {

        JSONObject json = new JSONObject();
        for( Map.Entry<String, String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }
}
