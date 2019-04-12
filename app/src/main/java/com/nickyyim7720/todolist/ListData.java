package com.nickyyim7720.todolist;

public class ListData {

    private String title, urgent, content, date;
    private int id;

    //---Constructor
    public ListData(){

    }

    public ListData(int id, String title, String urgent, String content, String date) {
        this.id = id;
        this.title = title;
        this.urgent = urgent;
        this.content = content;
        this.date = date;

    }

    //---getter and setter
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
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

    public void setDate(String dat){
        date = dat;
    }
    public String getDate(){
        return date;
    }
    //---End getter and setter

}
