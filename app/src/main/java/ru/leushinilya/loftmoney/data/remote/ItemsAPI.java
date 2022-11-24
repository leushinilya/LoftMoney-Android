package ru.leushinilya.loftmoney.data.remote;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemsAPI {

    @GET("./items")
    Single<ArrayList<RemoteItem>> getItems(@Query("type") String type, @Query("auth-token") String authToken);

    @POST("./items/add")
    @FormUrlEncoded
    Completable postItems(@Field("price") float price, @Field("name") String name, @Field("type") String type, @Field("auth-token") String authToken);

    @POST("./items/remove")
    @FormUrlEncoded
    Completable removeItem(@Field("id") String id, @Field("auth-token") String authToken);

    @GET("./auth")
    Single<AuthResponse> makeLogin(@Query("social_user_id") String userId);

}
