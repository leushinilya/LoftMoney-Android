package ru.skillbranch.loftmoney;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.skillbranch.loftmoney.cells.ItemsAdapter;
import ru.skillbranch.loftmoney.cells.MoneyItem;

public class MainActivity extends AppCompatActivity {

    RecyclerView itemsView;
    ItemsAdapter itemsAdapter = new ItemsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureRecyclerView();
        generateMoney();
    }

    private void configureRecyclerView() {
        itemsView = findViewById(R.id.itemsView);
        itemsView.setAdapter(itemsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        itemsView.setLayoutManager(layoutManager);
    }

    private void generateMoney() {
        List<MoneyItem> moneyItemList = new ArrayList<>();
        moneyItemList.add(new MoneyItem("PS5", "50000"));
        moneyItemList.add(new MoneyItem("PS4", "20000"));
        itemsAdapter.setData(moneyItemList);
    }


}