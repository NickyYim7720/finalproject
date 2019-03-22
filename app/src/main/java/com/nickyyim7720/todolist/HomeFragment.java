package com.nickyyim7720.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    Context context;
    private View view;
    private TextView uName, fp_code;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        uName = (TextView) view.findViewById(R.id.uName);
        //fp_code = (TextView) view.findViewById(R.id.fp_code);

        /*String Uname = Model.getPref("Uname", context.getApplicationContext());
        if (Uname.matches("")){
            uName.setText(R.string.user_name);
            //fp_code.setText(R.string.fp_code);
        }else{
            uName.setText(Model.getPref("Uname", context.getApplicationContext()));
            //fp_code.setText(Model.getPref("fp_code", context.getApplicationContext()));
        }*/


        return view;
    }

}
