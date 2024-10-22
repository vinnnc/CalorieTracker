package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUpFragment extends Fragment {
     View vSignUp;

     public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         vSignUp = inflater.inflate(R.layout.fragment_sign_up, container, false);

         DatePicker dpDob = vSignUp.findViewById(R.id.dpDob);
         dpDob.setMaxDate(Calendar.getInstance().getTimeInMillis());

         final Spinner spActivityLv = vSignUp.findViewById(R.id.spActivityLv);
         final List<String> activityLvList = new ArrayList<>();
         activityLvList.add("1 - Extremely inactive");
         activityLvList.add("2 - Sedentary");
         activityLvList.add("3 - Moderately active");
         activityLvList.add("4 - Vigorously active");
         activityLvList.add("5 - Extremely active");
         ArrayAdapter<String> activityLvAdapter = new ArrayAdapter<>(getActivity(),
                 android.R.layout.simple_spinner_item, activityLvList);
         activityLvAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spActivityLv.setAdapter(activityLvAdapter);
         spActivityLv.setSelection(0);

         final Spinner spPostcode = vSignUp.findViewById(R.id.spPostcode);
         final List<String> postcodeList = new ArrayList<>();
         postcodeList.add("3000 - Melbourne City");
         postcodeList.add("3006 - South Bank");
         postcodeList.add("3141 - South Yarra");
         postcodeList.add("3142 - Toorak");
         postcodeList.add("3145 - Caulfield East");
         postcodeList.add("3162 - Caulfield");
         postcodeList.add("3163 - Canegie");
         postcodeList.add("3168 - Clayton");
         ArrayAdapter<String> postcodeAdapter = new ArrayAdapter<>(getActivity(),
                 android.R.layout.simple_spinner_item, postcodeList);
         postcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spPostcode.setAdapter(postcodeAdapter);
         spPostcode.setSelection(0);

         Button btnLogin = vSignUp.findViewById(R.id.btnLogin);
         btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FragmentManager fragmentManager = getFragmentManager();
                 fragmentManager.beginTransaction().replace(R.id.content_frame,
                         new LoginFragment()).commit();
             }
         });

         Button btnSignUp = vSignUp.findViewById(R.id.btnSignUp);
         btnSignUp.setOnClickListener(new View.OnClickListener() {

             @SuppressLint("SetTextI18n")
             @Override
             public void onClick(View v) {
                 SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();
                 EditText etUsername = vSignUp.findViewById(R.id.etUsername);
                 EditText etPassword = vSignUp.findViewById(R.id.etPassword);
                 EditText etPassword2 = vSignUp.findViewById(R.id.etPassword2);
                 EditText etFirstName = vSignUp.findViewById(R.id.etFirstName);
                 EditText etSurname = vSignUp.findViewById(R.id.etSurname);
                 EditText etEmail = vSignUp.findViewById(R.id.etEmail);
                 DatePicker dpDob = vSignUp.findViewById(R.id.dpDob);
                 EditText etHeight = vSignUp.findViewById(R.id.etHeight);
                 EditText etWeight = vSignUp.findViewById(R.id.etWeight);
                 RadioGroup rgGender = vSignUp.findViewById(R.id.rgGender);
                 EditText etAddress = vSignUp.findViewById(R.id.etAddress);
                 Spinner spPostcode = vSignUp.findViewById(R.id.spPostcode);
                 Spinner spActivityLv = vSignUp.findViewById(R.id.spActivityLv);
                 EditText etStepPerMile = vSignUp.findViewById(R.id.etStepPerMile);
                 String username = etUsername.getText().toString();
                 String password = etPassword.getText().toString();
                 String password2 = etPassword2.getText().toString();
                 String firstName = etFirstName.getText().toString();
                 String surname = etSurname.getText().toString();
                 String email = etEmail.getText().toString();
                 String dob = dpDob.getYear() + "-" + dpDob.getMonth() + "-"
                         + dpDob.getDayOfMonth();
                 String height = etHeight.getText().toString();
                 String weight = etWeight.getText().toString();
                 RadioButton rbGender = vSignUp.findViewById(rgGender.getCheckedRadioButtonId());
                 String gender = rbGender.getText().toString();
                 String address = etAddress.getText().toString();
                 String postcode = spPostcode.getSelectedItem().toString().substring(0,4);
                 String activityLv = spActivityLv.getSelectedItem().toString().substring(0,1);
                 String stepPerMile = etStepPerMile.getText().toString();
                 boolean allGood = true;
                 if (username.isEmpty()) {
                     etUsername.setError("Username cannot be empty");
                     allGood = false;
                 }
                 if (password.isEmpty()) {
                     etPassword.setError("Password cannot be empty");
                     allGood = false;
                 } else {
                     if (!password.matches("^[a-zA-Z]\\w{3,14}$")) {
                         etPassword.setError("Password is invalid, please try another one.");
                         allGood = false;
                     } else {
                         if (!password.equals(password2)) {
                             etPassword.setError("Two passwords do not match, please try again");
                             allGood = false;
                         }
                     }
                 }
                 if (firstName.isEmpty()) {
                     etFirstName.setError("Full name cannot be empty");
                     allGood = false;
                 }
                 if (surname.isEmpty()) {
                     etSurname.setError("Surname cannot be empty");
                     allGood = false;
                 }
                 if (email.isEmpty()) {
                     etEmail.setError("Email cannot be empty");
                     allGood = false;
                 }
                 if (!email.matches("^.+@[^.].*\\.[a-z]{2,}$")) {
                     etEmail.setError("Email is invalid, please try again");
                     allGood = false;
                 }
                 if (height.isEmpty()) {
                     etHeight.setError("Height cannot be empty");
                     allGood = false;
                 } else {
                     int heightInt = Integer.valueOf(height);
                     if (heightInt < 50 || heightInt > 260) {
                         etHeight.setError("Height is invalid, please try again " +
                                 "(between 50 and 260)");
                         allGood = false;
                     }
                 }
                 if (weight.isEmpty()) {
                     etWeight.setError("Weight cannot be empty");
                     allGood = false;
                 } else {
                     int weightInt = Integer.valueOf(weight);
                     if (weightInt < 20 || weightInt > 650) {
                         etWeight.setError("Weight is invalid, please try again " +
                                 "(between 20 and 650)");
                         allGood = false;
                     }
                 }
                 if (address.isEmpty()) {
                     etAddress.setError("Address cannot be empty");
                     allGood = false;
                 }
                 if (stepPerMile.isEmpty()) {
                     etStepPerMile.setError("Step per mile cannot be empty");
                     allGood = false;
                 }
                 if (allGood)
                     signUpAsyncTask.execute(username, RestClient.getHash(password), firstName,
                             surname, email, dob, height, weight, gender, address, postcode,
                             activityLv, stepPerMile);
                 else {
                     TextView tvSignUpFeedback = vSignUp.findViewById(R.id.tvSignUpFeedback);
                     tvSignUpFeedback.setText("Sign up failed!");
                 }
             }
         });

         return vSignUp;
     }

    @SuppressLint("StaticFieldLeak")
    private class SignUpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String findUsername = RestClient.findCredentialByUsername(params[0]);
            String findEmail = RestClient.findUserByEmail(params[4]);
            if (!findUsername.equals("[]"))
                return "Username is already exist, please try another one.";
            if (!findEmail.equals("[]"))
                return "Email is already exist, please try another one.";
            Users users = new Users();
            int userId = RestClient.count("users") + 1;
            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                users = new Users(userId, params[2], params[3], params[4], sdf.parse(params[5]),
                        Integer.valueOf(params[6]), Integer.valueOf(params[7]), params[8],
                        params[9], Integer.valueOf(params[10]), Integer.valueOf(params[11]),
                        Integer.valueOf(params[12]));
                Credential credential = new Credential(params[0], users, params[1]);
                RestClient.create("users", users);
                RestClient.create("credential", credential);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getActivity(), SecondActivity.class);
            Bundle bundle = new Bundle();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonUsers = gson.toJson(users);
            bundle.putString("jsonUsers", jsonUsers);
            intent.putExtras (bundle);
            startActivity(intent);
            return "";
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String response) {
            TextView tvSignUpFeedback = vSignUp.findViewById(R.id.tvSignUpFeedback);
            EditText etUsername = vSignUp.findViewById(R.id.etUsername);
            EditText etEmail = vSignUp.findViewById(R.id.etEmail);
            if (response.equals("Username is already exist, please try another one."))
                etUsername.setError(response);
            if (response.equals("Email is already exist, please try another one."))
                etEmail.setError(response);
            tvSignUpFeedback.setText(response);
        }
    }
}
