package ru.skillbranch.loftmoney.cells;

public class MoneyItem {

    private String Title, Value;

    public MoneyItem(String title, String value) {
        Title = title;
        Value = value;
    }

    public String getTitle() {
        return Title;
    }

    public String getValue() {
        return Value;
    }
}
