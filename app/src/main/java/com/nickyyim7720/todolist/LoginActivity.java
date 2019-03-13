package com.nickyyim7720.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity===>";
    private String login_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate()");

    }

    public void btnLoginClick(View v){
        Log.d(TAG, "btnLoginClick()");
        Model.setPref("LOGIN_CODE", "1", getApplicationContext());
        this.finish();
    }



}
