package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    View vHome;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvWelcome = vHome.findViewById(R.id.tv_welcome);
        Bundle bundle = getActivity().getIntent(). getExtras();
        String jsonUsers = bundle.getString("jsonUsers");
        String firstName = "";
        int userId = 0;
        try {
            JSONObject jsonObject = new JSONObject(jsonUsers);
            firstName = jsonObject.getString("firstname");
            userId = jsonObject.getInt("userid");
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        tvWelcome.setText("Hi, " + firstName);

        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String currentTime = sdfTime.format(Calendar.getInstance().getTime());
        TextView tvTime = vHome.findViewById(R.id.tv_time);
        tvTime.setText(currentTime);

        TextView tvGoal = vHome.findViewById(R.id.tv_goal);
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences("dailyGoal_" + userId + "_" + firstName,
                        Context.MODE_PRIVATE);

        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdfDate.format(Calendar.getInstance().getTime());

        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor spEditor = sharedPref.edit();
        String date = sharedPref.getString("date", "");
        if ((date).equals(currentDate)) {
            int goal = sharedPref.getInt("goal", 0);
            tvGoal.setText(goal + " KJ");
        }
        else {
            spEditor.putString("date", currentDate);
            spEditor.putInt("goal", 0);
            spEditor.apply();
            tvGoal.setText(0 + " KJ");
        }

        Button btnConfirm = vHome.findViewById(R.id.btn_set);
        final int finalUserId = userId;
        final String finalFirstName = firstName;
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText etGoal = vHome.findViewById(R.id.et_goal);
                if (etGoal.getText().toString().isEmpty()) {
                    etGoal.setError("Daily calorie goal cannot be empty.");
                    return;
                }

                int goal = Integer.valueOf(etGoal.getText().toString());
                SharedPreferences sharedPref = getActivity()
                        .getSharedPreferences("dailyGoal_" + finalUserId + "_"
                                        + finalFirstName, Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putString("date", sdfDate.format(Calendar.getInstance().getTime()));
                spEditor.putInt("goal", goal);
                spEditor.apply();
                TextView tvGoal = vHome.findViewById(R.id.tv_goal);
                tvGoal.setText(goal + " KJ");
            }
        });

        return vHome;
    }
}
