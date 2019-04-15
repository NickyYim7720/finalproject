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

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static String TAG = "MainActivity===>";
    private String login_code, user_name, fp_code;
    private FragmentTransaction ft;
    private ListData listData[];
    private String title, content, urgent, date;
    private int id;

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
                case R.id.navigation_upload:
                    openUploadPage();
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
            this.finish();
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new HomeFragment(user_name));
            ft.commit();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    private void openUploadPage(){
        startActivity(new Intent(this, UploadActivity.class));
    }
    private void loadHomeFrag() {
        Log.d(TAG, "loadHomeFrag()");
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment(user_name));
        ft.commit();
    }

    private void loadListFrag() {
        Log.d(TAG, "loadListFrag()");
        listData = Model.getList();
        ListAdapter listAdapter = new ListAdapter(listData);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new ListFragment(listAdapter));
        ft.commit();
    }

    private void downloadList(){
        Log.d(TAG, "downloadList()");
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.d(TAG, "downloadList()->network OK");
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
        init();
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
            String _id = params[1];
            int response_code;
            String response = "";

            Log.d(TAG, "dl_ListAST->_server = " + _server + ", _uid = " + _id);

            try {
                URL url = new URL(_server);
                HttpURLConnection httpURLC = (HttpURLConnection) url.openConnection();
                httpURLC.setRequestMethod("POST");
                httpURLC.setConnectTimeout(5000);
                httpURLC.setDoOutput(true);
                httpURLC.setDoInput(true);

                //set POST data
                String data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(_id, "UTF-8");


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
                    for (int i = 0; i<size; i++){
                        JSONObject object = jarray.getJSONObject(i);
                        id = Integer.parseInt(object.getString("ID"));
                        title = object.getString("TITLE");
                        content = object.getString("CONTENT");
                        urgent = object.getString("URGENT");
                        date = object.getString("DATE");
                        list[i] = new ListData(id, title, urgent, content, date);
                    }
                    //set the data into Model.list
                    Model.setList(list);
                    Log.d(TAG, "list = " + list);

                    //Popup message for user
                    Toast.makeText(context.getApplicationContext(), _message, Toast.LENGTH_SHORT).show();

                    //load list fragment
                    loadListFrag();

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