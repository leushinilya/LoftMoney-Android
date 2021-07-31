package ru.leushinilya.loftmoney;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity implements TextWatcher {

    Button addButton;
    EditText nameEditText, priceEditText;
    int inputColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addButton = findViewById(R.id.add_button);
        nameEditText = findViewById(R.id.name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);

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

        if (getIntent().getIntExtra("currentPosition", 0) == 1) {
            inputColor = getResources().getColor(R.color.apple_green);
        } else inputColor = getResources().getColor(R.color.dark_sky_blue);
        nameEditText.setTextColor(inputColor);
        priceEditText.setTextColor(inputColor);
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

    private void setAddButtonStatus() {
        if (!nameEditText.getText().toString().equals("") && !priceEditText.getText().toString().equals("")) {
            addButton.setTextColor(inputColor);
            addButton.setClickable(true);
            addButton.getCompoundDrawables()[0].setTint(inputColor);

        } else {
            addButton.setTextColor(getResources().getColor(R.color.white_three));
            addButton.setClickable(false);
            addButton.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.white_three));
        }
    }
}