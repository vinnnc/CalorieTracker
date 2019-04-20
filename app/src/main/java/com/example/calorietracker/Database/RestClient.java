package com.example.calorietracker.Database;


import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RestClient {
    private final static String BASE_URL =
            "http://118.139.80.42:8080/CalorieTrackerWS/webresources/";

    public static String findCredentialByUsernameAndPasswordhash(String username, String password)
    {
        final String methodPath = "restws.credential/findByUsernameAndPasswordhash/" + username +
                "/" + password;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        List<JSONObject> jsonObjects = new ArrayList<>();
        //Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }
}
