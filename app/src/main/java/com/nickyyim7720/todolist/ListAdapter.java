package com.nickyyim7720.todolist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private String TAG = "ListAdpater===>";
    private ListData[] listData;
    private String urgent;

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
        viewHolder.txtViewDate.setText(listData[position].getDate());
        urgent = listData[position].getUrgent();
        if(urgent.matches("N")){
            viewHolder.imgViewIcon.setImageResource(android.R.drawable.star_big_off);
        }else if(urgent.matches("Y")){
            viewHolder.imgViewIcon.setImageResource((android.R.drawable.star_big_on));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "onBindViewHolder()->onClick("+position+"):"+listData[position].getTitle());
                Model.showDialog(v.getContext(), listData[position].getTitle(), listData[position]. getContent());
            }
        });

        //Long click for delete the record
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Toast.makeText(v.getContext(), "Delete "+listData[position].getTitle(), Toast.LENGTH_LONG).show();
                //MainActivity.deleteRecord(listData[position].getId());
                return true;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtViewTitle, txtViewContent, txtViewDate;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView){
            super(itemLayoutView);
            txtViewTitle = itemLayoutView.findViewById(R.id.item_title);
            txtViewContent = itemLayoutView.findViewById(R.id.item_content);
            txtViewDate = itemLayoutView.findViewById(R.id.item_date);
            imgViewIcon = itemLayoutView.findViewById(R.id.item_icon);
        }
    }

    @Override
    public int getItemCount(){
        return listData.length;
    }

}
