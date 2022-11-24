package ru.leushinilya.loftmoney.data.remote;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    //    {"status":"success","id": id_юзера, “auth_token”: auth-token}
//    в случае ошибки {"status":"Текст_ошибки"}

    @SerializedName("status")
    private String status;

    @SerializedName("id")
    private String id;

    @SerializedName("auth_token")
    private String authToken;

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getAuthToken() {
        return authToken;
    }
}
