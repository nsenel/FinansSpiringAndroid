package com.example.numan.finans;

/**
 * Created by numan on 8.09.2017.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.Arrays;

/**
 * Created by EmirCivas on 22.8.2017.
 */

public class Request
{
    private static String HOST = "http://10.0.2.2:8084/Finans/kurlar";
    //private static String HOST = "https://api.fixer.io";
    private static String connect(String endPoint, String... params) throws Exception
    {
        Log.e("x","Request Adr : "+HOST+"/"+endPoint);
        Log.e("x","Request Params : "+ Arrays.toString(params));

        String jsonStr = Jsoup.connect(HOST+"/"+endPoint)
                .ignoreContentType(true)
                .timeout(30000)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .data(params)
                .get()
                .text()
                .trim();

        Log.e("x","Response : \n"+jsonStr);

        return jsonStr;
    }

    public static JSONObject getObject(String endPoint, String... params) throws Exception
    {
        return new JSONObject(connect(endPoint,params));
    }

    public static JSONArray getArray(String endPoint, String... params) throws Exception
    {
        return new JSONArray(connect(endPoint,params));
    }
}

