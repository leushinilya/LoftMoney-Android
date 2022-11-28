package ru.leushinilya.loftmoney.data.remote.service

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import ru.leushinilya.loftmoney.data.remote.entity.RemoteItem

interface ItemsService {

    @GET("./items")
    fun getItems(@Query("type") type: String): Single<List<RemoteItem>>

    @POST("./items/add")
    @FormUrlEncoded
    fun postItems(
        @Field("price") price: Float,
        @Field("name") name: String,
        @Field("type") type: String
    ): Completable

    @POST("./items/remove")
    @FormUrlEncoded
    fun removeItem(@Field("id") id: String): Completable

}