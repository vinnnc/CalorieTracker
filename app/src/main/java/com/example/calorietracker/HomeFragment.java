package com.example.calorietracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    View vHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvWelcome = vHome.findViewById(R.id.tv_welcome);
        Bundle bundle = getActivity().getIntent(). getExtras();
        String welcome = "Hi, " + bundle.getString("firstName");
        tvWelcome.setText(welcome);
        return vHome;
    }
}
