package com.nickyyim7720.todolist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static String TAG = "MainActivity===>";
    private String login_code, user_name, fp_code;
    private FragmentTransaction ft;
    private ListData listData[];
    private String title, content, urgent, date;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.PI);
                    loadHomeFrag();
                    return true;
                case R.id.navigation_list:
                    mTextMessage.setText(R.string.ToDoList);
                    downloadList();
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

    private void init() {
        Log.d(TAG, "init()");
        login_code = Model.getPref("LOGIN_CODE", getApplicationContext());
        Log.d(TAG, "login_code = " + login_code);
        user_name = Model.getPref("UNAME", getApplicationContext());
        Log.d(TAG, "user_name = " + user_name);
        //fp_code = Model.getPref("fp_code", getApplicationContext());
        if (login_code.matches("") || login_code.matches("0") || user_name.matches("") /* || fp_code.matches("")*/) {
            Model.setPref("LOGIN_CODE", "0", getApplicationContext());
            login_code = "0";
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new HomeFragment(user_name));
            ft.commit();
        }
    }

    private void loadHomeFrag() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new HomeFragment(user_name));
        ft.commit();
    }

    private void loadListFrag() {
        ListAdapter listAdapter = new ListAdapter(listData);
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new ListFragment(listAdapter));
        ft.commit();
    }

    private void downloadList(){
        Log.d(TAG, "downloadList()");
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.d(TAG, "login()->network OK");
            String _uid = Model.getPref("UID", getApplicationContext());
            String _address = Model.getPref("SERVER", getApplicationContext());
            String _server = "http://" + _address + "/php/dl_list.php";
            Log.d(TAG, "server path = " + _server);

            try {
                new MainActivity.dl_ListAST(getApplicationContext()).execute(_server, _uid);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "no network");
            Toast.makeText(this, getResources().getString(R.string.msg_network_disconnect), Toast.LENGTH_SHORT).show();
        }
    }
    // ==== Logout ====
    public void logout() {
        Log.d(TAG, "logout()");
        Model.setPref("LOGIN_CODE", "0", getApplicationContext());
        Model.setPref("UNAME", "", getApplicationContext());
        Model.setPref("UID", "", getApplicationContext());
        startActivity(new Intent(this, LoginActivity.class));
    }


    public class dl_ListAST extends AsyncTask<String, Void, String> {

        //Properties
        Context context;

        //Constructor
        dl_ListAST(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String _server = params[0];
            String _uid = params[1];
            int response_code;
            String response = "";

            Log.d(TAG, "dl_ListAST->_server = " + _server + ", _uid = " + _uid);

            try {
                URL url = new URL(_server);
                HttpURLConnection httpURLC = (HttpURLConnection) url.openConnection();
                httpURLC.setRequestMethod("POST");
                httpURLC.setConnectTimeout(5000);
                httpURLC.setDoOutput(true);
                httpURLC.setDoInput(true);

                //set POST data
                String data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(_uid, "UTF-8");

                //send POST data
                OutputStream outputS = httpURLC.getOutputStream();
                BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outputS, "UTF-8"));
                buffW.write(data);
                buffW.flush();
                buffW.close();
                outputS.close();

                //get webpage response code
                response_code = httpURLC.getResponseCode();
                if (response_code == 200) {
                    //recevice Server response
                    InputStream inputS = httpURLC.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inputS, "iso-8859-1"));
                    response = "";
                    String line = "";
                    while ((line = buffR.readLine()) != null) {
                        response += line;
                    }
                    buffR.close();
                    inputS.close();
                    httpURLC.disconnect();
                }
                return response;

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //JSON Parse
            result = Model.parseJSON(result);
            Log.d(TAG, "parse JSON");
            try {
                JSONObject jsonO = new JSONObject(result);
                if ((String.valueOf(jsonO.optString("STATUS")).matches("1"))) {
                    String _message = jsonO.optString("MESSAGE");
                    Log.d(TAG, _message);
                    JSONArray jarray = jsonO.getJSONArray("todo");
                    //Set the parameters for looping
                    int size = jarray.length();
                    ListData list[] = new ListData[size];
                    //Converse String date to date format
                    Date convDate;
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    ParsePosition pos = new ParsePosition(0);
                    for (int i = 0; i<size; i++){
                        JSONObject object = jarray.getJSONObject(i);
                        title = object.getString("TITLE");
                        content = object.getString("CONTENT");
                        urgent = object.getString("URGENT");
                        date = object.getString("date");
                        convDate = format.parse(date, pos);
                        list[i] = new ListData(title, urgent, content, convDate);
                    }
                    //put the list into listData
                    listData = list;
                    loadListFrag();

                    //Popup message for user
                    Toast.makeText(context.getApplicationContext(), _message, Toast.LENGTH_SHORT).show();

                } else {
                    String _message = jsonO.optString("MESSAGE");
                    Log.d(TAG, _message);

                    //Popup message for user
                    Toast.makeText(context.getApplicationContext(), _message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } //JSON Parse

        }

    }
}