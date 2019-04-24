package com.example.calorietracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalorieTrackerFragment extends Fragment {
    View vCalorieTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vCalorieTracker = inflater.inflate(R.layout.fragment_calorie_tracker, container,
                false);
        return vCalorieTracker;
    }
}
