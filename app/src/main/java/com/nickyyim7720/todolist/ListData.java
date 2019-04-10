package com.nickyyim7720.todolist;

import java.util.Date;

public class ListData {

    private String title, urgent, content;
    private Date date;

    //---Constructor
    public ListData(){

    }

    public ListData(String title, String urgent, String content, Date date) {
        this.title = title;
        this.urgent = urgent;
        this.content = content;
        this.date = date;
    }

    //---getter and setter
    public void setTitle(String tit) {
        title = tit;
    }
    public String getTitle() {
        return title;
    }

    public void setUrgent(String urg){
        urgent = urg;
    }
    public String getUrgent(){
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
