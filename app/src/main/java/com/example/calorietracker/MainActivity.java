package com.example.calorietracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spActivityLv = findViewById(R.id.spActivityLv);
        final List<String> activityLvList = new ArrayList<>();
        activityLvList.add("1 - Extremely inactive");
        activityLvList.add("2 - Sedentary");
        activityLvList.add("3 - Moderately active");
        activityLvList.add("4 - Vigorously active");
        activityLvList.add("5 - Extremely active");
        ArrayAdapter<String> activityLvAdapter = new ArrayAdapter<>(this, android.R
                .layout.simple_spinner_item, activityLvList);
        activityLvAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActivityLv.setAdapter(activityLvAdapter);
        spActivityLv.setSelection(0);

        final Spinner spPostcode = findViewById(R.id.spPostcode);
        final List<String> postcodeList = new ArrayList<>();
        postcodeList.add("3000 - Melbourne City");
        postcodeList.add("3006 - South Bank");
        postcodeList.add("3141 - South Yarra");
        postcodeList.add("3145 - Caulfield East");
        postcodeList.add("3800 - Clayton");
        ArrayAdapter<String> postcodeAdapter = new ArrayAdapter<>(this, android.R
                .layout.simple_spinner_item, postcodeList);
        postcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostcode.setAdapter(postcodeAdapter);
        spPostcode.setSelection(0);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                EditText etUsername = findViewById(R.id.etUsername);
                EditText etPassword = findViewById(R.id.etPassword);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!username.isEmpty() && !password.isEmpty())
                    loginAsyncTask.execute(username, password);
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();
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
                Spinner spPostcode = findViewById(R.id.spPostcode);
                Spinner spActivityLv = findViewById(R.id.spActivityLv);
                EditText etStepPerMile = findViewById(R.id.etStepPerMile);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String firstName = etFirstName.getText().toString();
                String surname = etSurname.getText().toString();
                String email = etEmail.getText().toString();
                String dob = dpDob.getYear() + "-" + dpDob.getMonth() + "-" + dpDob.getDayOfMonth();
                String height = etHeight.getText().toString();
                String weight = etWeight.getText().toString();
                RadioButton rbGender = findViewById(rgGender.getCheckedRadioButtonId());
                String gender = rbGender.getText().toString();
                String address = etAddress.getText().toString();
                String postcode = spPostcode.getSelectedItem().toString().substring(0,4);
                String activityLv = spActivityLv.getSelectedItem().toString().substring(0,1);
                String stepPerMile = etStepPerMile.getText().toString();
                if (username.isEmpty())
                    etUsername.setError("Username cannot be null");
                if (password.isEmpty())
                    etPassword.setError("Password cannot be null");
                if (firstName.isEmpty())
                    etFirstName.setError("Full name cannot be null");
                if (surname.isEmpty())
                    etSurname.setError("Surname cannot be null");
                if (email.isEmpty())
                    etEmail.setError("Email cannot be null");
                if (height.isEmpty())
                    etHeight.setError("Height cannot be null");
                if (weight.isEmpty())
                    etWeight.setError("Weight cannot be null");
                if (address.isEmpty())
                    etAddress.setError("Address cannot be null");
                if (stepPerMile.isEmpty())
                    etStepPerMile.setError("Step per mile cannot be null");
                if (!(username.isEmpty() || password.isEmpty() || firstName.isEmpty() || surname
                        .isEmpty() || email.isEmpty() || height.isEmpty() || weight.isEmpty() ||
                        gender.isEmpty() || address.isEmpty() || stepPerMile.isEmpty()))
                    signUpAsyncTask.execute(username, password, firstName, surname, email, dob,
                            height, weight, gender, address, postcode, activityLv,stepPerMile);
            }
        });
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = RestClient.findCredentialByUsernameAndPasswordhash(params[0],
                    params[1]);
            if (result.equals("[]"))
                return "Username or password is incorrect, please try again";
            else {
                int userId = -1;
                String firstName = "";
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonCredentialObject = jsonArray.getJSONObject(0);
                    JSONObject jsonUsersObject = jsonCredentialObject.getJSONObject("userid");
                    userId = jsonUsersObject.getInt("userid");
                    firstName = jsonUsersObject.getString("firstname");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                Bundle bundle= new Bundle();
                bundle.putInt("userId", userId);
                bundle.putString("firstName", firstName);
                intent.putExtras (bundle);
                startActivity(intent);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            TextView tvLoginFeedback = findViewById(R.id.tvLoginFeedback);
            tvLoginFeedback.setText(response);
        }
    }

    private class SignUpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String findUsername = RestClient.findCredentialByUsername(params[0]);
            String findCredential = RestClient.findCredentialByUsernameAndPasswordhash(params[0],
                    params[1]);
            if (!findUsername.equals("[]")){
                EditText etUsername = findViewById(R.id.etUsername);
                etUsername.setError("Username is already exist");
                return "Sign Up failed";
            }
            if (!findCredential.equals("[]")){
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                loginAsyncTask.execute(params[0], params[1]);
                return null;
            }
            int userId = RestClient.countUsers() + 1;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Users users = new Users(userId, params[2], params[3], params[4],
                        sdf.parse(params[5]), Integer.valueOf(params[6]),
                        Integer.valueOf(params[7]), params[8], params[9],
                        Integer.valueOf(params[10]), Integer.valueOf(params[11]),
                        Integer.valueOf(params[12]));
                Credential credential = new Credential(params[0], userId, params[1]);
                RestClient.createUsers(users);
                RestClient.createCredential(credential);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            Bundle bundle= new Bundle();
            bundle.putInt("userId", userId);
            bundle.putString("firstName", params[2]);
            intent.putExtras (bundle);
            startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            TextView tvSignUpFeedback = findViewById(R.id.tvSignUpFeedback);
            tvSignUpFeedback.setText(response);
        }
    }
}
