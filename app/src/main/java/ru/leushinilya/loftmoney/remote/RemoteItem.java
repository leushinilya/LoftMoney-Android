package ru.leushinilya.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class RemoteItem {

//[{"id":”1”,"name":"","price":10, “type”:”expense”,"date":"2015-01-23"}]}

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("type")
    private String type;

    @SerializedName("date")
    private String date;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
