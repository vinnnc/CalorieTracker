package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calorietracker.Database.Step;
import com.example.calorietracker.Database.StepDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class StepsFragment extends Fragment {
    StepDatabase db;
    View vSteps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vSteps = inflater.inflate(R.layout.fragment_steps, container, false);
        final Bundle bundle = getActivity().getIntent(). getExtras();
        assert bundle != null;
        String jsonUsers = bundle.getString("jsonUsers");
        int userId = 0;
        String firstName = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonUsers);
            firstName = jsonObject.getString("firstname");
            userId = jsonObject.getInt("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase_" + userId + "_" + firstName)
                .fallbackToDestructiveMigration().build();
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        Button btnAdd = vSteps.findViewById(R.id.btn_add_record);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etStep = vSteps.findViewById(R.id.et_step);
                if (etStep.getText().toString().isEmpty())
                    etStep.setError("Step cannot be empty.");
                else {
                    InsertDatabase insertDatabase = new InsertDatabase();
                    insertDatabase.execute(Integer.valueOf(etStep.getText().toString()));
                    ReadDatabase readDatabase = new ReadDatabase();
                    readDatabase.execute();
                }
            }
        });

        Button btnEdit = vSteps.findViewById(R.id.btn_edit_record);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etId = vSteps.findViewById(R.id.et_id);
                EditText etStep = vSteps.findViewById(R.id.et_step);
                if (etId.getText().toString().isEmpty()) {
                    etId.setError("ID cannot be empty.");
                    return;
                }
                if (etStep.getText().toString().isEmpty()) {
                    etStep.setError("Step cannot be empty");
                    return;
                }
                int id = Integer.valueOf(etId.getText().toString());
                int step = Integer.valueOf(etStep.getText().toString());
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute(id, step);
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        });

        return vSteps;
    }

    @SuppressLint("StaticFieldLeak")
    private class ReadDatabase extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            List<Step> steps = db.stepDao().getAll();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
            if (!(steps.isEmpty() || steps == null)) {
                StringBuilder allSteps = new StringBuilder();
                String currentDate = sdfDate.format(Calendar.getInstance().getTime());
                for (Step step : steps) {
                    try {
                        String dateStr = sdfDate.format(sdfDate.parse(step.getTime()));
                        if (currentDate.equals(dateStr)) {
                            String stepStr = "    ID: " + step.getSid()
                                    + "    Time: " + step.getTime()
                                    + "    Step: " + step.getStep() + ";\n";
                            allSteps.append(stepStr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return allSteps.toString();
            }
            return "    This is not data.";
        }

        @Override
        protected void onPostExecute(String details) {
            TextView tvRecords = vSteps.findViewById(R.id.tv_records);
            tvRecords.setText(details);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertDatabase extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String time = sdf.format(Calendar.getInstance().getTime());
            Step step = new Step(time, params[0]);
            db.stepDao().insert(step);
            return "Add successful";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateDatabase extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            Step step;
            int id = params[0];
            step = db.stepDao().findByID(id);
            step.setStep(params[1]);
            if (step != null) {
                db.stepDao().updateStep(step);
                return "Edit successful";
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.equals("Edit successful"))
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT)
                        .show();
            else{
                EditText etID = vSteps.findViewById(R.id.et_id);
                etID.setError("ID doesn't exist!");
            }
        }
    }
}
