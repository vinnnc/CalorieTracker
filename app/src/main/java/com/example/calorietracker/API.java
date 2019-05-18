package com.example.calorietracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class API {
    private static final String GOOGLE_API_KEY = "AIzaSyCUapVuGj91DsfsdeJ3cDpnXtZ39QhyQJk";

    private static final String SEARCH_ID_CX = "008965496564990309248:zrf4tftxeb4";

    private static final String FOOD_API_ID = "e97ff207";

    private static final String FOOD_API_KEY = "b43dfc50dc08ddf02b9f5198632dd576";

    public static String newFood(String keyword) {
        URL url;
        HttpURLConnection connection = null;
        StringBuilder textResult = new StringBuilder();
        try {
            url = new URL("https://api.edamam.com/api/food-database/parser?ingr=" + keyword
                    + "&app_id=" + FOOD_API_ID + "&app_key=" + FOOD_API_KEY);
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
            }
        }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_API_KEY
                    + "&cx=" + SEARCH_ID_CX + "&q=" + keyword + query_parameter);
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

    public static String nearParks(double latitude, double longitude) {
        URL url;
        HttpURLConnection connection = null;
        StringBuilder textResult = new StringBuilder();
        try {
            url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + latitude + "," + longitude +
                    "&radius=5000&types=park&sensor=true&key=" + GOOGLE_API_KEY);
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

    public static String getSnippet(String result,int index) {
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > index)
                snippet =jsonArray.getJSONObject(index).getString("snippet");
        }catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        if (snippet.contains("\n"))
            snippet = snippet.replaceAll("\n", "");
        if (snippet.contains("...")) {
            snippet = snippet.replaceAll("\\.\\.\\.", "");
            String[] strings = snippet.split("\\.");
            snippet = "";
            for (int i = 0; i < strings.length - 1; i++)
                snippet += strings[i];
            snippet += ".";
        }
        return snippet;
    }

    public static String getImageSrc(String result, int index) {
        String imageSrc = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > index) {
                jsonObject = jsonArray.getJSONObject(index).getJSONObject("pagemap");
                jsonArray = jsonObject.getJSONArray("cse_thumbnail");
                imageSrc = jsonArray.getJSONObject(0).getString("src");
            }
        }catch (Exception e){
            e.printStackTrace();
            imageSrc = "NO INFO FOUND";
        }
        return imageSrc;
    }
}
