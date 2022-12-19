package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.data.remote.entity.RemoteItem

interface ItemsRepository {

    suspend fun getItems(type: String): List<RemoteItem>

    suspend fun postItem(price: Float, name: String, type: String)

    suspend fun removeItem(id: String)

}