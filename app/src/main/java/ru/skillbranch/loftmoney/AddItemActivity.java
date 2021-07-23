package ru.skillbranch.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

import ru.skillbranch.loftmoney.cells.Item;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    Button addButton;
    EditText nameEditText, priceEditText;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addButton = findViewById(R.id.add_button);
        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        addButton.setOnClickListener(this);
        type = getIntent().getExtras().getString("type");
    }

    public Item createItem() {
        String name = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        return new Item(name, price, type);
    }

    @Override
    public void onClick(View v) {
        ArrayList<Item> itemList = (ArrayList<Item>) getIntent().getExtras().getSerializable("itemList");
        itemList.add(createItem());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if(type.equals("income")){
            intent.putExtra("incomesItemList", itemList);
        } else if (type.equals("expense")){
            intent.putExtra("expensesItemList", itemList);
        }
        startActivity(intent);

    }
}