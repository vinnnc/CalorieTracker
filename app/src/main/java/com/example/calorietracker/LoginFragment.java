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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calorietracker.Database.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {
    View vLogin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vLogin = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = vLogin.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                EditText etUsername = vLogin.findViewById(R.id.etUsername);
                EditText etPassword = vLogin.findViewById(R.id.etPassword);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.isEmpty())
                    etUsername.setError("Username cannot be empty.");
                if (password.isEmpty())
                    etPassword.setError("Password cannot be empty.");
                if (!username.isEmpty() && !password.isEmpty())
                    loginAsyncTask.execute(username, password);
            }
        });

        Button btnSignUp = vLogin.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new SignUpFragment()).commit();
            }
        });
        return vLogin;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = RestClient.findCredentialByUsernameAndPasswordHash(params[0],
                    params[1]);
            if (result.equals("[]"))
                return "Username or password is incorrect, please try again!";

            Intent intent = new Intent(getActivity(), SecondActivity.class);
            Bundle bundle = new Bundle();
            try {
                JSONObject jsonCredential = new JSONArray(result).getJSONObject(0);
                JSONObject jsonUsers = jsonCredential.getJSONObject("userid");
                bundle.putString("jsonUsers", jsonUsers.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            intent.putExtras(bundle);
            startActivity(intent);
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            TextView tvLoginFeedback = vLogin.findViewById(R.id.tvLoginFeedback);
            tvLoginFeedback.setText(response);
        }
    }
}
