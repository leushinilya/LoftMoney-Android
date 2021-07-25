package ru.leushinilya.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity implements TextWatcher {

    Button addButton;
    EditText nameEditText, priceEditText;
    TextView nameTint, priceTint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addButton = findViewById(R.id.add_button);
        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        nameTint = findViewById(R.id.name_tint);
        priceTint = findViewById(R.id.price_tint);

        nameEditText.addTextChangedListener(this);
        priceEditText.addTextChangedListener(this);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", nameEditText.getText().toString());
                intent.putExtra("price", priceEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setAddButtonStatus();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        setAddButtonStatus();
    }

    private void setAddButtonStatus(){
        if (!nameEditText.getText().toString().equals("") && !priceEditText.getText().toString().equals("")) {
            addButton.setTextColor(getResources().getColor(R.color.apple_green));
            addButton.setClickable(true);

        } else {
            addButton.setTextColor(getResources().getColor(R.color.white_three));
            addButton.setClickable(false);
        }

        if(nameEditText.getText().toString().equals("")){
            nameTint.setVisibility(View.INVISIBLE);
        } else nameTint.setVisibility(View.VISIBLE);

        if(priceEditText.getText().toString().equals("")){
            priceTint.setVisibility(View.INVISIBLE);
        } else priceTint.setVisibility(View.VISIBLE);
    }
}