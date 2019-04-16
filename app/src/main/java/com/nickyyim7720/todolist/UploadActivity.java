package com.nickyyim7720.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    private String TAG = "UploadActivity===>";
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime, txtTitle, txtContent;
    Switch urgentSwitch;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String _uid, _title, _content, _urgent, _date, _time;
    private final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        this.setFinishOnTouchOutside(true);

        //Match the items with layout items
        btnDatePicker = (Button)findViewById(R.id.btn_datePick);
        btnTimePicker = (Button)findViewById(R.id.btn_timePick);
        txtDate = (EditText)findViewById(R.id.txtDate);
        txtTime = (EditText)findViewById(R.id.txtTime);
        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtContent = (EditText)findViewById(R.id.txtContent);
        urgentSwitch = (Switch)findViewById(R.id.urgentSwitch);

        //Set the Date and Time editText hints to current date and time
        txtDate.setHint(c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH));
        txtTime.setHint(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":00");

        //Set floating action button listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    public void btn_datePick_Click(View v){
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Because the date picker extsis a error in month, so i have to use +1 to fix the error.
                int month = monthOfYear + 1;
                txtDate.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void btn_timePick_Click(View v){
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtTime.setText(hourOfDay + ":" + minute + ":00");
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void btn_sub_Click(View v){
        Log.d(TAG, "btn_sub_Click()");
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.d(TAG, "btn_sub_Click()->network OK");
            _uid = Model.getPref("UID", getApplicationContext());
            _title = txtTitle.getText().toString();
            _content = txtContent.getText().toString();
            _date = txtDate.getText().toString();
            _time = txtTime.getText().toString();
            if(urgentSwitch.isChecked()){
                _urgent = "Y";
            }else{
                _urgent = "N";
            }
            String _address = Model.getPref("SERVER", getApplicationContext());
            String _server = "http://" + _address + "/php/upload.php";
            Log.d(TAG, "server path = " + _server);

            try {
                new UploadActivity.uploadAST(getApplicationContext()).execute(_server, _uid, _title, _content, _date, _time, _urgent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        } else {
            Log.d(TAG, "no network");
            Toast.makeText(this, getResources().getString(R.string.msg_network_disconnect), Toast.LENGTH_SHORT).show();
        }
    }

    public class uploadAST extends AsyncTask<String, Void, String> {

        //Properties
        Context context;

        //Constructor
        uploadAST(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String _server = params[0];
            String _uid = params[1];
            String _title = params[2];
            String _content = params[3];
            String _date = params[4];
            String _time = params[5];
            String _urgent = params[6];
            int response_code;
            String response = "";

            Log.d(TAG, "uploadAST->_server = " + _server + ", _uid = " + _uid + ", _title = " + _title);
            Log.d(TAG, "uploadAST-> _content = " + _content + ", _date = " + _date + ", _time = " + _time + ", _urgent = " + _urgent);

            try {
                URL url = new URL(_server);
                HttpURLConnection httpURLC = (HttpURLConnection) url.openConnection();
                httpURLC.setRequestMethod("POST");
                httpURLC.setConnectTimeout(5000);
                httpURLC.setDoOutput(true);
                httpURLC.setDoInput(true);

                //set POST data
                String data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(_uid, "UTF-8");
                data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(_title, "UTF-8");
                data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(_content, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(_date, "UTF-8");
                data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(_time, "UTF-8");
                data += "&" + URLEncoder.encode("urgent", "UTF-8") + "=" + URLEncoder.encode(_urgent, "UTF-8");


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

                    //Popup message for user
                    Toast.makeText(context.getApplicationContext(), _message, Toast.LENGTH_SHORT).show();

                    //Close upload dialog
                    finish();

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
