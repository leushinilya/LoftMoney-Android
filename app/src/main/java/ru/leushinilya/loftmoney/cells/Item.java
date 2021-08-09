package ru.leushinilya.loftmoney.cells;

import ru.leushinilya.loftmoney.remote.RemoteItem;

public class Item {

    private final String id, name, price;
//    0 - expense, 1 -income
    int type;
    private boolean isSelected;

    public Item(String id, String name, String price, int type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public static Item getInstance(RemoteItem remoteItem) {
        String remoteItemId = remoteItem.getId();
        String remoteItemName = remoteItem.getName();
        String remoteItemPrice = "" + remoteItem.getPrice();
        int remoteItemType;
        if(remoteItem.getType().equals("expense")) remoteItemType = 0; else remoteItemType = 1;
        return new Item(remoteItemId, remoteItemName, remoteItemPrice, remoteItemType);
    }
}
