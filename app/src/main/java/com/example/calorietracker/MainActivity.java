package com.example.calorietracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calorietracker.Database.RestClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnLogin = findViewById(R.id.btnLoginOrSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();

                EditText etUsername = findViewById(R.id.etUsername);
                EditText etPassword = findViewById(R.id.etPassword);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    loginAsyncTask.execute(username, password);
                }
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            } });
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result;
            result = RestClient.findCredentialByUsernameAndPasswordhash(params[0], params[1]);
            if (result.equals("[]")) {
                return "Username or password is incorrect, please enter again.";
            }
            else {
                return "login successful";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            TextView tvLoginResult = findViewById(R.id.tvLoginResult);
            tvLoginResult.setText(response);
        }
    }
}
