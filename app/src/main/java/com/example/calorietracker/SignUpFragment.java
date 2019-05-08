package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignUpFragment extends Fragment {
     View vSignUp;

     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         vSignUp = inflater.inflate(R.layout.fragment_sign_up, container, false);
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
         postcodeList.add("3145 - Caulfield East");
         postcodeList.add("3800 - Clayton");
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
                 String firstName = etFirstName.getText().toString();
                 String surname = etSurname.getText().toString();
                 String email = etEmail.getText().toString();
                 String dob = dpDob.getYear() + "-" + dpDob.getMonth() + "-" + dpDob.getDayOfMonth();
                 String height = etHeight.getText().toString();
                 String weight = etWeight.getText().toString();
                 RadioButton rbGender = vSignUp.findViewById(rgGender.getCheckedRadioButtonId());
                 String gender = rbGender.getText().toString();
                 String address = etAddress.getText().toString();
                 String postcode = spPostcode.getSelectedItem().toString().substring(0,4);
                 String activityLv = spActivityLv.getSelectedItem().toString().substring(0,1);
                 String stepPerMile = etStepPerMile.getText().toString();
                 if (username.isEmpty())
                     etUsername.setError("Username cannot be empty");
                 if (password.isEmpty())
                     etPassword.setError("Password cannot be empty");
                 if (firstName.isEmpty())
                     etFirstName.setError("Full name cannot be empty");
                 if (surname.isEmpty())
                     etSurname.setError("Surname cannot be empty");
                 if (email.isEmpty())
                     etEmail.setError("Email cannot be empty");
                 if (height.isEmpty())
                     etHeight.setError("Height cannot be empty");
                 if (weight.isEmpty())
                     etWeight.setError("Weight cannot be empty");
                 if (address.isEmpty())
                     etAddress.setError("Address cannot be empty");
                 if (stepPerMile.isEmpty())
                     etStepPerMile.setError("Step per mile cannot be empty");
                 if (!(username.isEmpty() || password.isEmpty() || firstName.isEmpty()
                         || surname.isEmpty() || email.isEmpty() || height.isEmpty()
                         || weight.isEmpty() || gender.isEmpty() || address.isEmpty()
                         || stepPerMile.isEmpty()))
                     signUpAsyncTask.execute(username, password, firstName, surname, email, dob,
                             height, weight, gender, address, postcode, activityLv,stepPerMile);
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
            CheckAvailableAsyncTask checkAvailableAsyncTask = new CheckAvailableAsyncTask();
            try {
                String checkResult = checkAvailableAsyncTask.execute(params[0], params[4]).get();
                if (!checkResult.isEmpty())
                    return "Sign up failed";
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int userId = RestClient.countUsers() + 1;
            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Users users = new Users(userId, params[2], params[3], params[4],
                        sdf.format(sdf.parse(params[5])), Integer.valueOf(params[6]),
                        Integer.valueOf(params[7]), params[8], params[9],
                        Integer.valueOf(params[10]), Integer.valueOf(params[11]),
                        Integer.valueOf(params[12]));
                Credential credential = new Credential(params[0], userId, params[1]);
                RestClient.createUsers(users);
                RestClient.createCredential(credential);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getActivity(), SecondActivity.class);
            Bundle bundle= new Bundle();
            bundle.putInt("userId", userId);
            bundle.putString("firstName", params[2]);
            intent.putExtras (bundle);
            startActivity(intent);
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            TextView tvSignUpFeedback = vSignUp.findViewById(R.id.tvSignUpFeedback);
            tvSignUpFeedback.setText(response);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CheckAvailableAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String findUsername = RestClient.findCredentialByUsername(params[0]);
            String findEmail = RestClient.findUserByEmail(params[1]);
            if (!findUsername.equals(""))
                return "Username is already exist, please try another one.";
            if (!findEmail.equals(""))
                return "Email is already exist, please try another one.";
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            EditText etUsername = vSignUp.findViewById(R.id.etUsername);
            EditText etEmail = vSignUp.findViewById(R.id.etEmail);
            if (response.equals("Username is already exist, please try another one."))
                etUsername.setError(response);
            if (response.equals("Email is already exist, please try another one."))
                etEmail.setError(response);
        }
    }
}
