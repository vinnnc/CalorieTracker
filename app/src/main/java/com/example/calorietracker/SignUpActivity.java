package com.example.calorietracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.calorietracker.Database.Credential;
import com.example.calorietracker.Database.RestClient;
import com.example.calorietracker.Database.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Spinner spActivityLv = findViewById(R.id.spActivityLv);
        final List<String> activityLvList = new ArrayList<>();
        activityLvList.add("1 - Extremely inactive");
        activityLvList.add("2 - Sedentary");
        activityLvList.add("3 - Moderately active");
        activityLvList.add("4 - Vigorously active");
        activityLvList.add("5 - Extremely active");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, activityLvList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActivityLv.setAdapter(spinnerAdapter);
        spActivityLv.setSelection(0);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = findViewById(R.id.etUsername);
                EditText etPassword = findViewById(R.id.etPassword);
                EditText etFirstName = findViewById(R.id.etFirstName);
                EditText etSurname = findViewById(R.id.etSurname);
                EditText etEmail = findViewById(R.id.etEmail);
                DatePicker dpDob = findViewById(R.id.dpDob);
                EditText etHeight = findViewById(R.id.etHeight);
                EditText etWeight = findViewById(R.id.etWeight);
                RadioGroup rgGender = findViewById(R.id.rgGender);
                EditText etAddress = findViewById(R.id.etAddress);
                EditText etPostcode = findViewById(R.id.etPostcode);
                EditText etStepPerMile = findViewById(R.id.etStepPerMile);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String firstName = etFirstName.getText().toString();
                String surname = etSurname.getText().toString();
                String email = etEmail.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dob = sdf.format(dpDob.getYear() + "-" + dpDob.getMonth() + "-" +
                        dpDob.getDayOfMonth());
                String height = etHeight.getText().toString();
                String weight = etWeight.getText().toString();
                RadioButton rbGender = findViewById(rgGender.getCheckedRadioButtonId());
                String gender = rbGender.getText().toString();
                String address = etAddress.getText().toString();
                String postcode = etPostcode.getText().toString();
                final String[] selectedActivityLv = {""};
                spActivityLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
                        selectedActivityLv[0] = String.valueOf(parent.getItemAtPosition(position)
                                .toString().charAt(0));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                String activityLv = selectedActivityLv[0];
                String stepPerMile = etStepPerMile.getText().toString();

                SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();
                if (username.isEmpty() && password.isEmpty() && firstName.isEmpty() && surname
                        .isEmpty() && email.isEmpty() && dob.isEmpty() && height.isEmpty() &&
                        weight.isEmpty() && gender.isEmpty() && address.isEmpty() && postcode
                        .isEmpty() && stepPerMile.isEmpty())
                    signUpAsyncTask.execute(username, password, firstName, surname, email, dob,
                            height, weight, gender, address, postcode, activityLv,stepPerMile);
            }
        });
    }

    private class SignUpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Credential credential = new Credential(params[0], Integer.valueOf(params[1]),
                    params[2]);
            try {
                Users users = new Users(Integer.valueOf(params[3]), params[4], params[5], params[6],
                        sdf.parse(params[7]), Integer.valueOf(params[8]), Integer.valueOf(params[9])
                        , params[10], params[11], Integer.valueOf(params[12]),
                        Integer.valueOf(params[13]), Integer.valueOf(params[14]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            RestClient.createCredential(credential);
            return "Credential was added";
        }

        @Override
        protected void onPostExecute(String response) {
            TextView tvSignUpResult = findViewById(R.id.tvSignUpResult);
            tvSignUpResult.setText("Successful");
        }
    }
}
