package ru.leushinilya.loftmoney.screens.main.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import ru.leushinilya.loftmoney.LoftApp;
import ru.leushinilya.loftmoney.R;
import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.cells.ItemsAdapter;

public class BudgetFragment extends Fragment {

    public final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerView itemsView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPosition;
    private BudgetViewModel budgetViewModel;
    private ItemsAdapter itemsAdapter = new ItemsAdapter();

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
        configureRecyclerView();
        configureRefreshLayout();
        configureViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        budgetViewModel.updateListFromInternet(
                ((LoftApp) getActivity().getApplication()).itemsAPI,
                currentPosition,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0));
    }

    private void configureRecyclerView() {

        itemsView = getView().findViewById(R.id.itemsView);
        itemsView.setAdapter(itemsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        itemsView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemsView.addItemDecoration(divider);

    }

    private void configureViewModel() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetViewModel.liveDataItems.observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                itemsAdapter.setData(items);
                System.out.println(items);
            }
        });
    }

    private void configureRefreshLayout() {
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                budgetViewModel.updateListFromInternet(
                        ((LoftApp) getActivity().getApplication()).itemsAPI,
                        currentPosition,
                        getActivity().getSharedPreferences(getString(R.string.app_name), 0));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}