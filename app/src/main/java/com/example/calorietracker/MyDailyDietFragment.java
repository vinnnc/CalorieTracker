package com.example.calorietracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyDailyDietFragment extends Fragment {
    View vMyDailyFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vMyDailyFragment = inflater.inflate(R.layout.fragment_my_daily_diet, container,
                false);
        return vMyDailyFragment;
    }
}
