package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    View vHome;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvWelcome = vHome.findViewById(R.id.tv_welcome);
        final Bundle bundle = getActivity().getIntent(). getExtras();
        assert bundle != null;
        final String firstName = bundle.getString("firstName");
        final int userId = bundle.getInt("userId");
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
                        .getSharedPreferences("dailyGoal_" + userId + "_" + firstName,
                                Context.MODE_PRIVATE);
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
