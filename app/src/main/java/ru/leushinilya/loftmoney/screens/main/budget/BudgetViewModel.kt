package ru.leushinilya.loftmoney.screens.main.budget;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.leushinilya.loftmoney.LoftApp;
import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.remote.ItemsAPI;
import ru.leushinilya.loftmoney.remote.RemoteItem;

public class BudgetViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<ArrayList<Item>> liveDataItems = new MutableLiveData<>(new ArrayList<Item>());
    public MutableLiveData<String> messageString = new MutableLiveData<>();
    public MutableLiveData<Integer> messageInt = new MutableLiveData<>();
    public MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public MutableLiveData<Float> incomesSum = new MutableLiveData<>(0f);
    public MutableLiveData<Float> expensesSum = new MutableLiveData<>(0f);

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public void updateListFromInternet(ItemsAPI itemsAPI, int currentPosition, SharedPreferences sharedPreferences) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        String type = "income";
        if (currentPosition == 0) type = "expense";
        Disposable disposable = (itemsAPI.getItems(type, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(remoteItems -> {
//                        sorting remoteItems list by date
                    Comparator<RemoteItem> comparator = (o1, o2) -> o1.getDate().compareTo(o2.getDate());
                    Collections.sort(remoteItems, comparator);

                    ArrayList<Item> itemList = new ArrayList<>();
                    float sum = 0;
                    for (RemoteItem remoteItem : remoteItems) {
                        itemList.add(Item.getInstance(remoteItem));
                        sum += remoteItem.getPrice();

                    }
                    liveDataItems.postValue(itemList);
                    if (currentPosition == 0) expensesSum.postValue(sum);
                    else if (currentPosition == 1) incomesSum.postValue(sum);

                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                }));

        compositeDisposable.add(disposable);
    }

    public void removeItem(Item item, ItemsAPI itemsAPI, SharedPreferences sharedPreferences) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        Disposable disposable = itemsAPI
                .removeItem(item.getId(), authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
//                    finish();
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                });
        compositeDisposable.add(disposable);
    }

}
