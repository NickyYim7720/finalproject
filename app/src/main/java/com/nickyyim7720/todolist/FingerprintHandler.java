package com.nickyyim7720.todolist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private Context context;

    public FingerprintHandler(Context context){
        this.context = context;
    }
    public void startAuthentication(FingerprintManager fpM, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fpM.authenticate(cryptoObject, cenCancellationSignal,0,this,null);

    }

    @Override
    public void onAuthenticationFailed(){
        super.onAuthenticationFailed();
        Toast.makeText(context, "FP Authentication Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result){
        super.onAuthenticationSucceeded(result);
    }
}
