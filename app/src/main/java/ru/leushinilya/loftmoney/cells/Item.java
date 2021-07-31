package ru.leushinilya.loftmoney.cells;

import ru.leushinilya.loftmoney.remote.RemoteItem;

public class Item {

    private final String name, price;
//    0 - expense, 1 -income
    int type;

    public Item(String name, String price, int type) {
        this.name = name;
        this.price = price;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getType() {
        return type;
    }

    public static Item getInstance(RemoteItem remoteItem) {
        String remoteItemName = remoteItem.getName();
        String remoteItemPrice = "" + remoteItem.getPrice();
        int remoteItemType;
        if(remoteItem.getType().equals("expense")) remoteItemType = 0; else remoteItemType = 1;
        return new Item(remoteItemName, remoteItemPrice, remoteItemType);
    }
}
