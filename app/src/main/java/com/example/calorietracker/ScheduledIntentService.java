package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.calorietracker.Database.Report;
import com.example.calorietracker.Database.RestClient;
import com.example.calorietracker.Database.Step;
import com.example.calorietracker.Database.StepDatabase;
import com.example.calorietracker.Database.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@SuppressLint("Registered")
public class ScheduledIntentService extends IntentService {

    DailyPostAsyncTask dailyPostAsyncTask;

    public ScheduledIntentService() {
        super("ScheduledIntentService");
        dailyPostAsyncTask = new DailyPostAsyncTask();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String jsonUsers = bundle.getString("jsonUsers");
        dailyPostAsyncTask.execute(jsonUsers);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @SuppressLint("StaticFieldLeak")
    public class DailyPostAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdfDate.format(Calendar.getInstance().getTime());
            String jsonUsers = strings[0];
            Users users = new Users();
            try {
                JSONObject jsonObject = new JSONObject(jsonUsers);
                users.setUserid(jsonObject.getInt("userid"));
                users.setFirstname(jsonObject.getString("firstname"));
                users.setSurname(jsonObject.getString("surname"));
                users.setEmail(jsonObject.getString("email"));
                String dobStr = jsonObject.getString("dob").split("T")[0];
                users.setDob(sdfDate.parse(dobStr));
                users.setHeight(jsonObject.getInt("height"));
                users.setWeight(jsonObject.getInt("weight"));
                users.setGender(jsonObject.getString("gender"));
                users.setAddress(jsonObject.getString("address"));
                users.setPostcode(jsonObject.getInt("postcode"));
                users.setActivitylv(jsonObject.getInt("activitylv"));
                users.setSteppermile(jsonObject.getInt("steppermile"));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            int repId = RestClient.count("report") + 1;
            int userId = users.getUserid();
            String firstName = users.getFirstname();
            SharedPreferences sharedPref = getSharedPreferences("dailyGoal_" + userId + "_"
                    + firstName, Context.MODE_PRIVATE);

            String date = sharedPref.getString("date", "");
            int goal = 0;
            if ((date).equals(currentDate))
                goal = sharedPref.getInt("goal", 0);

            StepDatabase db = Room.databaseBuilder(getApplicationContext(),
                    StepDatabase.class, "StepDatabase_" + userId + "_" + firstName)
                    .fallbackToDestructiveMigration().build();
            int totalSteps = 0;
            List<Step> steps = db.stepDao().getAll();
            if (!(steps.isEmpty() || steps == null)) {
                for (Step step : steps) {
                    date = step.getTime().substring(0, 10);
                    if (date.equals(currentDate))
                        totalSteps += step.getStep();
                }
            }

            int totalConsumed = 0;
            int totalBurned = 0;
            date = sdfDate.format(Calendar.getInstance().getTime());
            String result = RestClient.findTotalConsumedAndBurned(userId, date, goal, totalSteps);
            try {
                JSONObject jsonObject = new JSONObject(result);
                totalConsumed = jsonObject.getInt("totalconsumed");
                totalBurned = jsonObject.getInt("totalburned");
            } catch (Exception e) {
                e.printStackTrace();
                return "Daily post failed.";
            }

            Report report = new Report(repId, users, totalConsumed, totalBurned, totalSteps, goal);
            RestClient.create("report", report);
            db.stepDao().deleteAll();
            @SuppressLint("CommitPrefEdits")
            SharedPreferences.Editor spEditor = sharedPref.edit();
            spEditor.putString("date", "");
            spEditor.putInt("goal", 0);
            spEditor.apply();
            return "Daily post successful";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}
