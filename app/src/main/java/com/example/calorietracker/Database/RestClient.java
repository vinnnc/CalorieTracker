package com.example.calorietracker.Database;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class RestClient {
    private final static String BASE_URL =
            "http://10.0.2.2:8080/CalorieTrackerWS/webresources/";

    public static String findCredentialByUsernameAndPasswordHash(String username, String password)
    {
        String passwordHash = getHash(password);
        final String methodPath = "restws.credential/findByUsernameAndPasswordHash/" + username +
                "/" + passwordHash;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String findCredentialByUsername(String username)
    {
        final String methodPath = "restws.credential/findByUsername/" + username;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static void create(String name, Object object) {
        //initialise
        URL url;
        HttpURLConnection conn = null;
        final String methodPath = "restws." + name;
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUsersJson = gson.toJson(object);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUsersJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringUsersJson);
            out.close();
            Log.i("error", Integer.valueOf(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
    }

    public static int count(String type) {
        final String methodPath = "restws." + type +"/count";
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder testResult = new StringBuilder();

        //Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            Log.i("url", url.toString());
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "text/plain");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                testResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return Integer.valueOf(testResult.toString());
    }

    public static String findAllFood()
    {
        final String methodPath = "restws.food/";
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String findTotalConsumedAndBurned(int userId, String date, int goal,
                                                    int totalSteps) {
        final String methodPath = "restws.report/createReportA/"
                + userId + "/" + date + "/" + goal + "/" + totalSteps;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String findUserByEmail(String email)
    {
        final String methodPath = "restws.users/findByEmail/" + email;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String findFoodByFoodnameAndCategory(String foodname, String category)
    {
        final String methodPath = "restws.food/findByFoodnameAndCategory/"
                + foodname + "/" + category;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String findReportByUserIdAndDate(String userId, String date) {
        final String methodPath = "restws.report/findByUseridAndDate/"
                + userId + "/" + date;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String findPeriodReportByUseridAndStartdateAndEnddate(String userId,
                                                                        String startdate,
                                                                        String enddate) {
        final String methodPath = "restws.report/findByUseridAndStartdateAndEnddate/"
                + userId + "/" + startdate + "/" + enddate;
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuilder textResult = new StringBuilder();
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
                textResult.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }
        return textResult.toString();
    }

    public static String getHash(String str) {
        MessageDigest digest;
        byte[] input = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            input = digest.digest(str.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return convertToHex(input);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte datum : data) {
            int halfbyte = (datum >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = datum & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString().toUpperCase();
    }
}
