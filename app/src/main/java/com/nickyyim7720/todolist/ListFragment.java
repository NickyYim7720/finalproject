package com.nickyyim7720.todolist;
/*----------------------------------
   App name: todolist
       Created by: nicky yim
           Date: 10/4/2019
               Version: 1.0
 -----------------------------------*/
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListFragment extends Fragment {

    private Context context;
    private View view;
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;

    public ListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListFragment(ListAdapter adapter){
        this.listAdapter = adapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}
