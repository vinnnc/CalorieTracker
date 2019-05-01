package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
        String firstName = bundle.getString("firstName");
        tvWelcome.setText("Hi, " + firstName);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String currentTime = sdfTime.format(Calendar.getInstance().getTime());
        TextView tvTime = vHome.findViewById(R.id.tv_time);
        tvTime.setText(currentTime);
        Button btnConfirm = vHome.findViewById(R.id.btn_set);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText etGoal = vHome.findViewById(R.id.et_goal);
                int goal = Integer.valueOf(etGoal.getText().toString());
                TextView tvGoal = vHome.findViewById(R.id.tv_goal);
                tvGoal.setText("Daily calorie goal has been set to " + goal + "KJ");
            }
        });
        return vHome;
    }
}
