package ru.leushinilya.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InternetResponse {

    @SerializedName("status")
    String status;

    @SerializedName("data")
    ArrayList<RemoteItem> remoteItems;

    public String getStatus() {
        return status;
    }

    public ArrayList<RemoteItem> getRemoteItems() {
        return remoteItems;
    }
}
