package ru.leushinilya.loftmoney.screens.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import ru.leushinilya.loftmoney.LoftApp;
import ru.leushinilya.loftmoney.R;
import ru.leushinilya.loftmoney.screens.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        if(authToken!=null && !authToken.equals("")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }
}