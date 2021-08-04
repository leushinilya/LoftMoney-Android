package ru.leushinilya.loftmoney;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.leushinilya.loftmoney.remote.AuthAPI;
import ru.leushinilya.loftmoney.remote.ItemsAPI;

public class LoftApp extends Application {

    public ItemsAPI itemsAPI;
    public AuthAPI authAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        configureRetrofit();
    }

    private void configureRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://verdant-violet.glitch.me/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        itemsAPI = retrofit.create(ItemsAPI.class);
        authAPI = retrofit.create(AuthAPI.class);
    }
}
