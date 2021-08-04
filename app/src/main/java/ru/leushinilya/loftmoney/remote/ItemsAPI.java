package ru.leushinilya.loftmoney.remote;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemsAPI {

    @GET("./items")
    Single<ItemsResponse> getItems(@Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable postItems(@Field("price") String price, @Field("name") String name, @Field("type") String type);

}
