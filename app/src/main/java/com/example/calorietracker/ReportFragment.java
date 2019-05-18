package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.calorietracker.Database.RestClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportFragment extends Fragment {
    View vReport;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);

        DatePicker dpSingleDate = vReport.findViewById(R.id.dp_single_date);
        dpSingleDate.setMaxDate(Calendar.getInstance().getTimeInMillis());
        Button btnBarChart = vReport.findViewById(R.id.btn_bar_chart);
        btnBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new Report2Fragment()).commit();
            }
        });

        Button btnCheck1 =  vReport.findViewById(R.id.btn_check_1);
        btnCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker pdSingleDate = vReport.findViewById(R.id.dp_single_date);
                String monthStr;
                String dayStr;
                int month = pdSingleDate.getMonth() + 1;
                int day = pdSingleDate.getDayOfMonth();
                if (month < 10)
                    monthStr = "0" + month;
                else
                    monthStr = "" + month;

                if (day < 10)
                    dayStr = "0" + day;
                else
                    dayStr = "" + day;

                String singleDate = pdSingleDate.getYear() + "-" + monthStr + "-"
                        + dayStr;
                Bundle bundle = getActivity().getIntent(). getExtras();
                String jsonUsers = bundle.getString("jsonUsers");
                int userId = 0;
                try {
                    JSONObject jsonObject = new JSONObject(jsonUsers);
                    userId = jsonObject.getInt("userid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PieChartAsyncTask pieChartAsyncTask = new PieChartAsyncTask();
                pieChartAsyncTask.execute("" + userId, singleDate);
            }
        });

        return vReport;
    }

    @SuppressLint("StaticFieldLeak")
    private class PieChartAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findReportByUserIdAndDate(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.equals("[]")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "There is no data on that date.", Toast.LENGTH_SHORT).show();
                return;
            }
            PieChart chart = vReport.findViewById(R.id.pie_chart);
            chart.getDescription().setEnabled(false);
            chart.setEntryLabelColor(Color.BLACK);
            chart.setEntryLabelTextSize(12f);
            int totalConsumed = -1;
            int totalBurned = -1;
            int remaining = -1;
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                totalConsumed = jsonObject.getInt("totalcalconsumed");
                totalBurned = jsonObject.getInt("totalcalburned");
                int goal = jsonObject.getInt("calgoal");
                remaining = goal + totalConsumed - totalBurned;
                if (remaining < 0)
                    remaining = 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(totalConsumed, "Total Consumed"));
            entries.add(new PieEntry(totalBurned, "Total Burned"));
            entries.add(new PieEntry(remaining, "Remaining"));
            PieDataSet dataSet = new PieDataSet(entries, "");
            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            data.setValueTextSize(11f);
            chart.setData(data);
            chart.invalidate();
        }
    }
}
