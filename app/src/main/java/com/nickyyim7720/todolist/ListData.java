package com.nickyyim7720.todolist;

import java.util.Date;

public class ListData {

    private String title;
    private int urgent;
    private String content;
    private Date date;

    //---Constructor
    public ListData(){

    }

    public ListData(String title, int urgent) {
        this.title = title;
        this.urgent = urgent;
    }

    //---getter and setter
    public void setTitle(String tit) {
        title = tit;
    }
    public String getTitle() {
        return title;
    }

    public void setUrgent(int urg){
        urgent = urg;
    }
    public int getUrgent(){
        return urgent;
    }

    public void setContent(String cont){
        content = cont;
    }
    public String getContent(){
        return content;
    }

    public void setDate(Date dat){
        date = dat;
    }
    public Date getDate(){
        return date;
    }
    //---End getter and setter

}
