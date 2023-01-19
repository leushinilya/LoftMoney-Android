package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.data.remote.entity.Balance
import ru.leushinilya.loftmoney.data.remote.entity.RemoteItem
import ru.leushinilya.loftmoney.data.remote.service.ItemsService
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val itemsService: ItemsService
) : ItemsRepository {

    override suspend fun getItems(type: String): List<RemoteItem> = itemsService.getItems(type)

    override suspend fun postItem(price: Float, name: String, type: String) =
        itemsService.postItems(price, name, type)

    override suspend fun removeItem(id: String) = itemsService.removeItem(id)

    override suspend fun getBalance(): Balance = itemsService.getBalance()

}