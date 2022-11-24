package ru.leushinilya.loftmoney.cells

import ru.leushinilya.loftmoney.data.remote.RemoteItem

data class Item(
    val id: String,
    val name: String,
    val price: String, //    0 - expense, 1 -income
    var type: Int
) {
    companion object {
        fun getInstance(remoteItem: RemoteItem): Item {
            val remoteItemId = remoteItem.id
            val remoteItemName = remoteItem.name
            val remoteItemPrice = "" + remoteItem.price
            val remoteItemType: Int
            remoteItemType = if (remoteItem.type == "expense") 0 else 1
            return Item(remoteItemId, remoteItemName, remoteItemPrice, remoteItemType)
        }
    }
}