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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Report2Fragment extends Fragment {
    View vReport2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vReport2 = inflater.inflate(R.layout.fragment_report_2, container, false);

        DatePicker dpStart = vReport2.findViewById(R.id.dp_start);
        dpStart.setMaxDate(Calendar.getInstance().getTimeInMillis());
        DatePicker dpEnd = vReport2.findViewById(R.id.dp_end);
        dpEnd.setMaxDate(Calendar.getInstance().getTimeInMillis());

        Button btnPieChart = vReport2.findViewById(R.id.btn_pie_chart);
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new ReportFragment()).commit();
            }
        });

        Button btnCheck2 = vReport2.findViewById(R.id.btn_check_2);
        btnCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthStr;
                String dayStr;
                DatePicker dpStart = vReport2.findViewById(R.id.dp_start);
                int month = dpStart.getMonth() + 1;
                int day = dpStart.getDayOfMonth();
                if (month < 10)
                    monthStr = "0" + month;
                else
                    monthStr = "" + month;

                if (day < 10)
                    dayStr = "0" + day;
                else
                    dayStr = "" + day;

                String startDate = dpStart.getYear() + "-" + monthStr + "-" + dayStr;

                DatePicker dpEnd = vReport2.findViewById(R.id.dp_end);
                month = dpEnd.getMonth() + 1;
                day = dpEnd.getDayOfMonth();
                if (month < 10)
                    monthStr = "0" + month;
                else
                    monthStr = "" + month;

                if (day < 10)
                    dayStr = "0" + day;
                else
                    dayStr = "" + day;

                String endDate = dpEnd.getYear() + "-" + monthStr + "-" + dayStr;

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(endDate).before(sdf.parse(startDate))) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "End date cannot earlier than start date.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Bundle bundle = getActivity().getIntent().getExtras();
                String jsonUsers = bundle.getString("jsonUsers");
                int userId = 0;
                try {
                    JSONObject jsonObject = new JSONObject(jsonUsers);
                    userId = jsonObject.getInt("userid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BarChartAsyncTask barChartAsyncTask = new BarChartAsyncTask();
                barChartAsyncTask.execute("" + userId, startDate, endDate);
            }
        });

        return vReport2;
    }

    @SuppressLint("StaticFieldLeak")
    private class BarChartAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findPeriodReportByUseridAndStartdateAndEnddate(strings[0], strings[1],
                    strings[2]);
        }

        @Override
        protected void onPostExecute(String result) {
            BarChart chart = vReport2.findViewById(R.id.bar_chart);
            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);
            chart.getXAxis().setEnabled(false);

            if (result.equals("[]")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "There is no data during that period.", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<BarEntry> values1 = new ArrayList<>();
            ArrayList<BarEntry> values2 = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int totalConsumed = jsonObject.getInt("totalcalconsumed");
                    int totalBurned = jsonObject.getInt("totalcalburned");
                    values1.add(new BarEntry(i, totalConsumed));
                    values2.add(new BarEntry(i, totalBurned));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarDataSet set1, set2;
            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
                set1.setValues(values1);
                set2.setValues(values2);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values1, "Total Consumed");
                set1.setColor(Color.rgb(104, 241, 175));
                set2 = new BarDataSet(values2, "Total Burned");
                set2.setColor(Color.rgb(164, 228, 251));
                BarData data = new BarData(set1, set2);
                chart.setData(data);
            }
            chart.getBarData().setBarWidth(0.2f);

            // restrict the x-axis range
            chart.getXAxis().setAxisMinimum(1);

            chart.getXAxis().setAxisMaximum(1 + chart.getBarData().getGroupWidth(0.08f,
                    0.03f) * values1.size());
            chart.groupBars(1, 0.08f, 0.03f);
            chart.invalidate();
        }
    }
}
