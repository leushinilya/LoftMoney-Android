package ru.leushinilya.loftmoney;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity implements TextWatcher {

    Button addButton;
    EditText nameEditText, priceEditText;
    int inputColor;
    String type = "expense";
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
                putItemToInternet();
            }
        });

        if (getIntent().getIntExtra("currentPosition", 0) == 1) {
            inputColor = getResources().getColor(R.color.apple_green);
            type = "income";
        } else{
            inputColor = getResources().getColor(R.color.dark_sky_blue);
            type = "expense";
        }
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

    private void putItemToInternet() {
        Disposable disposable = ((LoftApp)getApplication()).internetAPI
                .postItems(priceEditText.getText().toString(), nameEditText.getText().toString(), type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    finish();
                }, throwable -> {
                    Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}