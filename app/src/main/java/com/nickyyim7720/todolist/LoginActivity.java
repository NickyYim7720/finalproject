package com.nickyyim7720.todolist;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity===>";
    private String login_code;
    private FingerprintManager fpM;
    private KeyguardManager kygM;
    private CancellationSignal cancellationSignal;
    private TextView txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate()");

        kygM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fpM = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if (!kygM.isKeyguardSecure()){
            //check the fingerprint screen lock able
            Log.d(TAG, "!isKeyguardSecure()");
            Toast.makeText(this, "Your smartphone haven't setting the fingerprint lock", LENGTH_SHORT).show();
            return;
        }

        if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED){
            if (!fpM.isHardwareDetected()){
                //check hardware contant the fingerprint hardware
                Log.d(TAG, "!isHardwareDetected()");
                Toast.makeText(this, "Your smartphone haven't install the fingerprint lock", LENGTH_SHORT).show();
                return;
            }

            if (!fpM.hasEnrolledFingerprints()){
                //check device at least has one fingerprint record
                Log.d(TAG, "!hasEnrolledFingerprints()");
                Toast.makeText(this, "Your smartphone haven't set the fingerprint record", LENGTH_SHORT).show();
                return;
            }
        }
        startFingerprintListening();


    }

    private void startFingerprintListening(){
        cancellationSignal = new CancellationSignal();
        if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED){
            fpM.authenticate(null, //wrapper class of crypto objects, it makes authentication more safty.
                              cancellationSignal, //for cancel authenticate object
                             0, //optional flags; should be 0
                              authenticationCallback, //callback for receive the authenticate success or fail
                             null); //optional value, if it able to use, fingerprintmanager will though it for sending message.
        }
    }

    FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback(){
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString){
            Log.d(TAG, "onAuthenticationError");
        }

        @Override
        public void onAuthenticationFailed(){
            Log.d(TAG, "onAuthenticationFailed");
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result){
            Log.d(TAG, "onAuthenticationSucceeded");
            login();
        }

    };

    @Override
    public void onPause(){
        super.onPause();
        cancellationSignal.cancel();
        cancellationSignal = null;
    }

    public void login(){
        Log.d(TAG, "login()");
        Model.setPref("LOGIN_CODE", "1", getApplicationContext());
        this.finish();
    }

}
