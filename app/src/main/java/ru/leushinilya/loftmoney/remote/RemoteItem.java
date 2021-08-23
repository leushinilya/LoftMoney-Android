package ru.leushinilya.loftmoney.remote;

import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoteItem {

//[{"id":”1”,"name":"","price":10, “type”:”expense”,"date":"2015-01-23"}]}

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private float price;

    @SerializedName("type")
    private String type;

    @SerializedName("created_at")
    private String date;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getStringDate() {
        return date;
    }

    public Date getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
