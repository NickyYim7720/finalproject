package com.nickyyim7720.todolist;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.net.URL;


public class WebFragment extends Fragment {

    private View view;
    private Context context;
    private WebView webView;
    private String url;

    public WebFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public WebFragment(String url){
        this.url = url;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web, container, false);

        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl(this.url);

        return view;
    }

}
