package com.example.zolup;

import java.io.Serializable;
import java.util.Date;

public class MapInfo implements Serializable{
    String docu_id;
    String my_id;
    String name;
    double latitude;
    double longitude;
    String content;
    int id;
    boolean check_share;
    Date date;

    public void setDocu_id(String docu_id){
        this.docu_id = docu_id;
    }

    public void setMy_id(String my_id){
        this.my_id = my_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setCheck_share(boolean check_share){
        this.check_share = check_share;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public String getDocu_id(){
        return this.docu_id;
    }

    public String getMy_id(){
        return this.my_id;
    }

    public String getName(){
        return this.name;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getContent(){
        return this.content;
    }

    public int getId() {
        return this.id;
    }

    public boolean getCheck_share(){
        return this.check_share;
    }

    public Date getDate(){
        return this.date;
    }
}