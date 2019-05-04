package com.example.calorietracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class API {
    private static final String GOOGLE_API_KEY = "AIzaSyCUapVuGj91DsfsdeJ3cDpnXtZ39QhyQJk";

    private static final String SEARCH_ID_CX = "008965496564990309248:trcyljwlnhu";

    public static String search(String keyword, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url;
        HttpURLConnection connection = null;
        StringBuilder textResult = new StringBuilder();
        StringBuilder query_parameter= new StringBuilder();
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter.append("&");
                query_parameter.append(params[i]);
                query_parameter.append("=");
                query_parameter.append(values[i]);
            } }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key="+
                    GOOGLE_API_KEY+ "&cx="+ SEARCH_ID_CX + "&q="+ keyword + query_parameter);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine())
                textResult.append(scanner.nextLine());
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult.toString();
    }

    public static String getSnippet(String result){
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0)
                snippet =jsonArray.getJSONObject(0).getString("snippet");
        }catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }
}
