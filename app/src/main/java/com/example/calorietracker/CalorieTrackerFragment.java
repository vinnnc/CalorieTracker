package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calorietracker.Database.RestClient;
import com.example.calorietracker.Database.Step;
import com.example.calorietracker.Database.StepDatabase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CalorieTrackerFragment extends Fragment {
    StepDatabase db;
    View vCalorieTracker;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vCalorieTracker = inflater.inflate(R.layout.fragment_calorie_tracker, container,
                false);

        final Bundle bundle = getActivity().getIntent(). getExtras();
        assert bundle != null;
        final int userId = bundle.getInt("userId");
        final String firstName = bundle.getString("firstName");
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences("dailyGoal_" + userId + "_" + firstName,
                        Context.MODE_PRIVATE);
        int goal = sharedPref.getInt("goal", 0);
        TextView tvGoal = vCalorieTracker.findViewById(R.id.tv_goal);
        tvGoal.setText("    Daily Calorie Goal: " + goal);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase_" + userId + "_" + firstName)
                .fallbackToDestructiveMigration().build();
        TotalStepAsyncTask totalStepAsyncTask = new TotalStepAsyncTask();
        totalStepAsyncTask.execute();

        TotalConsumedAndBurnedAsyncTask totalConsumedAndBurnedAsyncTask =
                new TotalConsumedAndBurnedAsyncTask();
        totalConsumedAndBurnedAsyncTask.execute("" + userId);

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

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(Calendar.getInstance().getTime());
            String result = RestClient.findTotalConsumedAndBurned(userId, date);
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
            tvTotalConsumed.setText("    Total Calorie Consumed: " + result[0]);
            tvTotalBurned.setText("    Total Calorie Burned: " + result[1]);
            if (Integer.valueOf(result[2]) < 0)
                result[2] = "0";
            tvRemain.setText("    Calorie Remaining: " + result[2]);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class TotalStepAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            int totalStep = 0;
            List<Step> steps = db.stepDao().getAll();
            if (!(steps.isEmpty() || steps == null)) {
                for (Step step : steps)
                    totalStep += step.getStep();
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
