package com.nickyyim7720.todolist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
            httpURLConnection.setRequestMethod("POST")
                    .setConnectTimeout(5000)
                    .setDoOutput(true)
                    .setDoInput(true);

            //set POST data
            String data = URLEncoder.encoder("userid", "UTF-8") + "=" + URLEncoder.encoder(_userid, "UTF-8");
                   data = URLEncoder.encoder("password", "UTF-8") + "=" + URLEncoder.encoder(_password, "UTF-8");

            //send POST data
            OutputStream outputS = httpURLC.getResponseCode();
            BufferdWriter buffW = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            buffW.write(data)
                    .flush()
                    .close();
            outputStream.close();

            //get webpage response code
            response_code = httpURLC.getResponseCode();
            if (response_code == 200){
                //recevice Server response
                InputStream inputS = httpURLC.getInputStream();
                BufferedReader buffR = new BufferedReader(new InputStreamReader(intputS, "iso-8859-1"));
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













    }

}
