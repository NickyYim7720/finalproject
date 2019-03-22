package com.nickyyim7720.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

public class HomeFragment extends Fragment {

    private static String TAG = "HomeFragment===>";
    private View view;
    private TextView uName, fp_code;
    private String user = "Guest";

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(String user){
        this.user = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        uName = (TextView) view.findViewById(R.id.uName);
        //fp_code = (TextView) view.findViewById(R.id.fp_code);


        Log.d(TAG, "user = " + user);
        if (user.matches("")){
            uName.setText(R.string.user_name);
            //fp_code.setText(R.string.fp_code);
        }else{
            uName.setText(user);
            //fp_code.setText(Model.getPref("fp_code", context.getApplicationContext()));
        }


        return view;
    }

}
