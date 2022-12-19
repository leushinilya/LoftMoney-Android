package ru.leushinilya.loftmoney.data.remote.service

import retrofit2.http.*
import ru.leushinilya.loftmoney.data.remote.entity.RemoteItem

interface ItemsService {

    @GET("./items")
    suspend fun getItems(@Query("type") type: String): List<RemoteItem>

    @POST("./items/add")
    @FormUrlEncoded
    suspend fun postItems(
        @Field("price") price: Float,
        @Field("name") name: String,
        @Field("type") type: String
    )

    @POST("./items/remove")
    @FormUrlEncoded
    suspend fun removeItem(@Field("id") id: String)

}