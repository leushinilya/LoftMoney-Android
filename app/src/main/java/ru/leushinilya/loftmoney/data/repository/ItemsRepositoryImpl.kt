package ru.leushinilya.loftmoney.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.leushinilya.loftmoney.data.remote.entity.RemoteItem
import ru.leushinilya.loftmoney.data.remote.service.ItemsService
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val itemsService: ItemsService
) : ItemsRepository {

    override fun getItems(type: String): Single<List<RemoteItem>> = itemsService.getItems(type)

    override fun postItem(price: Float, name: String, type: String): Completable =
        itemsService.postItems(price, name, type)

    override fun removeItem(id: String): Completable = itemsService.removeItem(id)

}