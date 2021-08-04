package ru.leushinilya.loftmoney.screens.main.budget;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.leushinilya.loftmoney.R;
import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.remote.ItemsAPI;
import ru.leushinilya.loftmoney.remote.RemoteItem;

public class BudgetViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MutableLiveData<ArrayList<Item>> liveDataItems = new MutableLiveData<>();
    MutableLiveData<String> messageString = new MutableLiveData<>();
    MutableLiveData<Integer> messageInt = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public void updateListFromInternet(ItemsAPI itemsAPI, int currentPosition) {
        String type = "income";
        if (currentPosition == 0) type = "expense";
        Disposable disposable = (itemsAPI.getItems(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(internetResponse -> {
                    if (internetResponse.getStatus().equals("success")) {
                        ArrayList<RemoteItem> remoteItems = internetResponse.getRemoteItems();
//                        sorting remoteItems list by date
                        Comparator<RemoteItem> comparator = (o1, o2) -> o1.getDate().compareTo(o2.getDate());
                        Collections.sort(remoteItems, comparator);

                        ArrayList<Item> itemList = new ArrayList<>();
                        for (RemoteItem remoteItem : remoteItems) {
                            itemList.add(Item.getInstance(remoteItem));
                        }
                        liveDataItems.postValue(itemList);
                    } else
                        messageInt.postValue(R.string.connection_lost);
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                }));

        compositeDisposable.add(disposable);
    }
}
