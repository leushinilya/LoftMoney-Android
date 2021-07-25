package ru.leushinilya.loftmoney.cells;

import java.io.Serializable;

public class Item implements Serializable {

    private final String name, price;
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
}
