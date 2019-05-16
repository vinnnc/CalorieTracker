package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calorietracker.Database.RestClient;
import com.example.calorietracker.Database.Step;
import com.example.calorietracker.Database.StepDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CalorieTrackerFragment extends Fragment {
    StepDatabase db;
    View vCalorieTracker;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        vCalorieTracker = inflater.inflate(R.layout.fragment_calorie_tracker, container,
                false);

        // Set the alarm to start at 23:59.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        Bundle bundle = getActivity().getIntent(). getExtras();
        assert bundle != null;
        String jsonUsers = bundle.getString("jsonUsers");
        int userId = 0;
        String firstName = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonUsers);
            firstName = jsonObject.getString("firstname");
            userId = jsonObject.getInt("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences("dailyGoal_" + userId + "_" + firstName,
                        Context.MODE_PRIVATE);
        int goal = sharedPref.getInt("goal", 0);
        TextView tvGoal = vCalorieTracker.findViewById(R.id.tv_goal);
        tvGoal.setText("    Daily Calorie Goal: " + goal + " KJ");

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase_" + userId + "_" + firstName)
                .fallbackToDestructiveMigration().build();
        TotalStepAsyncTask totalStepAsyncTask = new TotalStepAsyncTask();
        String totalSteps = "";
        try {
            totalSteps = totalStepAsyncTask.execute().get();
        } catch (ExecutionException |InterruptedException e) {
            e.printStackTrace();
        }

        TotalConsumedAndBurnedAsyncTask totalConsumedAndBurnedAsyncTask =
                new TotalConsumedAndBurnedAsyncTask();
        totalConsumedAndBurnedAsyncTask.execute("" + userId, "" + goal, totalSteps);

        AlarmManager alarmMgr =
                (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getActivity(), ScheduledIntentService.class);
        alarmIntent.putExtras (bundle);
        PendingIntent pendingIntent =
                PendingIntent.getService(getActivity(), 0, alarmIntent, 0);
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

//        Button btnForcePostReport = vCalorieTracker.findViewById(R.id.btn_force_post_report);
//        btnForcePostReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ScheduledIntentService scheduledIntentService = new ScheduledIntentService();
//                scheduledIntentService.dailyPostAsyncTask.execute();
//            }
//        });

        return vCalorieTracker;
    }

    @SuppressLint("StaticFieldLeak")
    private class TotalConsumedAndBurnedAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            int totalConsumed = 0;
            int totalBurned = 0;
            int remaining = 0;
            int userId = Integer.valueOf(params[0]);
            int goal = Integer.valueOf(params[1]);
            int totalSteps = Integer.valueOf(params[2]);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(Calendar.getInstance().getTime());
            String result = RestClient.findTotalConsumedAndBurned(userId, date, goal, totalSteps);
            try {
                JSONObject jsonObject = new JSONObject(result);
                totalConsumed = jsonObject.getInt("totalconsumed");
                totalBurned = jsonObject.getInt("totalburned");
                remaining = jsonObject.getInt("remaining");
            }catch (Exception e) {
                e.printStackTrace();
            }
            return totalConsumed + "; " + totalBurned + "; " + remaining;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String response) {
            TextView tvTotalConsumed = vCalorieTracker.findViewById(R.id.tv_total_consumed);
            TextView tvTotalBurned = vCalorieTracker.findViewById(R.id.tv_total_burned);
            TextView tvRemain = vCalorieTracker.findViewById(R.id.tv_remain);
            String[] result = response.split("; ");
            tvTotalConsumed.setText("    Total Calorie Consumed: " + result[0] + " KJ");
            tvTotalBurned.setText("    Total Calorie Burned: " + result[1] + " KJ");
            tvRemain.setText("    Calorie Remaining: " + result[2] + " KJ");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class TotalStepAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            int totalStep = 0;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String current = sdf.format(Calendar.getInstance().getTime());
            List<Step> steps = db.stepDao().getAll();
            if (!(steps.isEmpty() || steps == null)) {
                for (Step step : steps) {
                    String date = step.getTime().substring(0,10);
                    if (date.equals(current))
                        totalStep += step.getStep();
                }
            }
            return "" + totalStep;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String response) {
            TextView tvTotalStep = vCalorieTracker.findViewById(R.id.tv_total_steps);
            tvTotalStep.setText("    Total Daily Steps: " + response);
        }
    }
}
