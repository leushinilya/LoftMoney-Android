package ru.skillbranch.loftmoney.cells;

import java.io.Serializable;

public class Item implements Serializable {

    private final String name, price, type;

    public Item(String name, String price, String type) {
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

    public String getType() {
        return type;
    }
}
