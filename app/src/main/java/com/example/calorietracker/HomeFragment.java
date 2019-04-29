package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    View vHome;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvWelcome = vHome.findViewById(R.id.tv_welcome);
        Bundle bundle = getActivity().getIntent(). getExtras();
        assert bundle != null;
        String firstName = bundle.getString("firstName");
        tvWelcome.setText("Hi, " + firstName);

        String currentTime = Calendar.getInstance().getTime().toString();
        TextView tvTime = vHome.findViewById(R.id.tv_time);
        tvTime.setText("Current time is " + currentTime);
        return vHome;
    }
}
