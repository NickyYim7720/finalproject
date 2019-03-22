package com.nickyyim7720.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static String TAG = "MainActivity===>";
    private String login_code, user_name, fp_code;
    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.PI);
                    return true;
                case R.id.navigation_list:
                    mTextMessage.setText(R.string.ToDoList);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.Setting);
                    return true;
                case R.id.navigation_logout:
                    logout();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        init();
    }

    private void init(){
        Log.d(TAG, "init()");
        login_code = Model.getPref("LOGIN_CODE", getApplicationContext());
        Log.d(TAG, "login_code = " + login_code);
        user_name = Model.getPref("UNAME", getApplicationContext());
        Log.d(TAG, "user_name = " + user_name);
        //fp_code = Model.getPref("fp_code", getApplicationContext());
        if (login_code.matches("") || login_code.matches("0") || user_name.matches("") /* || fp_code.matches("")*/ ){
            Model.setPref("LOGIN_CODE", "0", getApplicationContext());
            login_code = "0";
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new HomeFragment(user_name));
            ft.commit();
        }
    }

    // ==== Logout ====
    public void logout(){
        Log.d(TAG, "logout()");
        Model.setPref("LOGIN_CODE", "0", getApplicationContext());
        Model.setPref("UNAME", "", getApplicationContext());
        startActivity(new Intent(this, LoginActivity.class));
    }

}
