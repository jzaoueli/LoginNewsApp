package com.jihed.loginnewsapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jihed.loginnewsapp.R;
import com.jihed.loginnewsapp.java.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserET;
    private EditText mPasswordET;

    private Button mLoginBtn;
    private Button mRegisterBtn;
    private JSONParser jsonParser = new JSONParser();
    private String LOGIN_URL = "http://192.168.2.123:8080/newss/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserET = (EditText) findViewById(R.id.usernameET);
        mUserET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event.getKeyCode() == 66 || actionId == EditorInfo.IME_ACTION_DONE) {
                    mPasswordET.requestFocus();
                }
                return false;
            }
        });
        mPasswordET = (EditText) findViewById(R.id.passwordET);
        mPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event.getKeyCode() == 66 || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    attempLogin(0);
                }
                return false;
            }
        });

        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempLogin(0);
            }
        });
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempLogin(1);
            }
        });
    }

    private void attempLogin(int btn) {
        String userName = mUserET.getText().toString();
        String password = mPasswordET.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            mUserET.setError(getString(R.string.user_name_error));
            return;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordET.setError(getString(R.string.password_error));
            return;
        }

        if (btn == 0)
            new LoginUserTask(userName, password).execute();
        else
            new LoginUserTask(userName, password).execute();

    }

    private class LoginUserTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String Username;
        private String Password;

        private String error;

        private LoginUserTask(String username, String password) {
            Username = username;
            Password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(LoginActivity.this,
                    "Connection...", "Check username and password", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", Username));
            pairs.add(new BasicNameValuePair("password", Password));

            jsonObjectResult = jsonParser.makeHttpRequest(LOGIN_URL, pairs);
            try {
                if (jsonObjectResult.getInt("success") == 1)
                    return true;
                else
                    error = jsonObjectResult.getString("message");

            } catch (Exception ex) {
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean) {
                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mIntent);
                Log.e("JSONObjectResult : ", jsonObjectResult +"");
            } else {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
