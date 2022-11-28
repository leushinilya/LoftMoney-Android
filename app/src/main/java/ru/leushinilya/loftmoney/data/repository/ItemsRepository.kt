package ru.leushinilya.loftmoney.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.leushinilya.loftmoney.data.remote.entity.RemoteItem

interface ItemsRepository {

    fun getItems(type: String): Single<List<RemoteItem>>

    fun postItem(price: Float, name: String, type: String): Completable

    fun removeItem(id: String): Completable

}