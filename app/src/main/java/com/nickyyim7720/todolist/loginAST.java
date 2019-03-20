package com.nickyyim7720.todolist;

import android.content.Context;
import android.widget.Toast;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class loginAST extends AsyncTask<String, Void, String> {

    private static String TAG = "loginAST===>";
    //Properties
    Context context;

    //Constructor
    loginAST(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... params){
        String _server   = params[0];
        String _userid   = params[1];
        String _password = params[2];
        int response_code;
        String response = "";

        Log.d(TAG, "loginAST->_server = " + _server + ", _userid = " + _userid + ", _password = " + _password);

        try {
            URL url = new URL(_server);
            HttpURLConnection httpURLC = (HttpURLConnection) url.openConnection();
            httpURLC.setRequestMethod("POST");
            httpURLC.setConnectTimeout(5000);
            httpURLC.setDoOutput(true);
            httpURLC.setDoInput(true);

            //set POST data
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(_userid, "UTF-8");
                   data += URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(_password, "UTF-8");

            //send POST data
            OutputStream outputS = httpURLC.getOutputStream();
            BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outputS, "UTF-8"));
            buffW.write(data);
            buffW.flush();
            buffW.close();
            outputS.close();

            //get webpage response code
            response_code = httpURLC.getResponseCode();
            if (response_code == 200){
                //recevice Server response
                InputStream inputS = httpURLC.getInputStream();
                BufferedReader buffR = new BufferedReader(new InputStreamReader(inputS, "iso-8859-1"));
                response = "";
                String line = "";
                while ((line = buffR.readLine()) != null){
                    response += line;
                }
                buffR.close();
                inputS.close();
                httpURLC.disconnect();
            }
            return response;

        }catch (MalformedURLException e){

            e.printStackTrace();
        }catch (IOException e){

            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result){
        //JSON Parse
        result = Model.parseJSON(result);
        try{
            JSONObject jsonO = new JSONObject(result);
            if ((String.valueOf(jsonO.optString("STATUS")).matches("1"))||
                    (String.valueOf(jsonO.optString("STATUS")).matches("2"))){
                String _status = jsonO.optString("STATUS");
                String _uid = jsonO.optString("NAME");
                String _message = jsonO.optString("MESSAGE");

                //Update Shared Preferences
                Model.setPref("LOGIN_CODE", "1", context.getApplicationContext());
                Log.d(TAG, "login_code = " + Model.getPref("LOGIN_CODE", context.getApplicationContext()));

                //Popup message for user
                Toast.makeText(context.getApplicationContext(), _message, Toast.LENGTH_SHORT).show();

            }else{
                String _message = jsonO.optString("MESSAGE");

                //Update Shared Preferences
                Model.setPref("LOGIN_CODE", "0", context.getApplicationContext());

                //Popup message for user
                Toast.makeText(context.getApplicationContext(), _message, Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException e){
            e.printStackTrace();
        } //JSON Parse

    }

}
