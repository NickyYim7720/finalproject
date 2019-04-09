package com.nickyyim7720.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.util.Log;
import android.widget.TextView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private String TAG = "ListAdpater===>";
    private ListData[] listData;

    public ListAdapter(ListData[] listData){
        this.listData = listData;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        viewHolder.txtViewTitle.setText(listData[position].getTitle());
        viewHolder.txtViewContent.setText(listData[position].getContent());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "onBindViewHolder()->onClick("+position+"):"+listData[position].getTitle());

                Toast.makeText(v.getContext(), position+"."+listData[position].getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtViewTitle;
        public TextView txtViewContent;

        public ViewHolder(View itemLayoutView){
            super(itemLayoutView);
            txtViewTitle = itemLayoutView.findViewById(R.id.item_title);
            txtViewContent = itemLayoutView.findViewById(R.id.item_content);
        }
    }

    @Override
    public int getItemCount(){
        return listData.length;
    }

}
