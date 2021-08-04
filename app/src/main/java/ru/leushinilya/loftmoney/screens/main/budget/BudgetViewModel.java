package ru.leushinilya.loftmoney.screens.main.budget;

import android.content.SharedPreferences;
import android.widget.Toast;

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
import ru.leushinilya.loftmoney.R;
import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.remote.ItemsAPI;
import ru.leushinilya.loftmoney.remote.RemoteItem;

public class BudgetViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MutableLiveData<ArrayList<Item>> liveDataItems = new MutableLiveData<>();
    MutableLiveData<String> messageString = new MutableLiveData<>();
    MutableLiveData<Integer> messageInt = new MutableLiveData<>();
    MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);

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
                        for (RemoteItem remoteItem : remoteItems) {
                            itemList.add(Item.getInstance(remoteItem));
                        }
                        liveDataItems.postValue(itemList);

                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                }));

        compositeDisposable.add(disposable);
    }

    public void removeItem(Item item, ItemsAPI itemsAPI, SharedPreferences sharedPreferences){
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
