package ru.leushinilya.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.cells.ItemsAdapter;
import ru.leushinilya.loftmoney.remote.RemoteItem;

public class BudgetFragment extends Fragment {

    final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerView itemsView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPosition;

    ItemsAdapter itemsAdapter = new ItemsAdapter();
    ArrayList<Item> itemList = new ArrayList<>();

    public static BudgetFragment newInstance(int position) {
        BudgetFragment budgetFragment = new BudgetFragment();
        budgetFragment.currentPosition = position;
        return budgetFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemsView = view.findViewById(R.id.itemsView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListFromInternet(currentPosition);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        configureRecyclerView();
        updateListFromInternet(currentPosition);
    }

    private void updateListFromInternet(int currentPosition) {
        String type = "income";
        if (currentPosition == 0) type = "expense";
        Disposable disposable = ((LoftApp) getActivity().getApplication()).internetAPI.getItems(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(internetResponse -> {
                    if (internetResponse.getStatus().equals("success")) {
                        ArrayList<RemoteItem> remoteItems = internetResponse.getRemoteItems();
//                        sorting remoteItems list by date
                        Comparator<RemoteItem>comparator = (o1, o2) -> o1.getDate().compareTo(o2.getDate());
                        Collections.sort(remoteItems, comparator);

                        itemList.clear();
                        for (RemoteItem remoteItem : remoteItems) {
                            itemList.add(Item.getInstance(remoteItem));
                        }
                        itemsAdapter.setData(itemList);
                    } else
                        Toast.makeText(getActivity(), R.string.connection_lost, Toast.LENGTH_LONG);
                }, throwable -> {
                    Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG);
                });

        compositeDisposable.add(disposable);
    }

    private void configureRecyclerView() {
        itemsView.setAdapter(itemsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        itemsView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemsView.addItemDecoration(divider);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String name = data.getStringExtra("name");
            String price = data.getStringExtra("price");
            itemList.add(new Item(name, price, currentPosition));
            itemsAdapter.setData(itemList);
        }
    }

}