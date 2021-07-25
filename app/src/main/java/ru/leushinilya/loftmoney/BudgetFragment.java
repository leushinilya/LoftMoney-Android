package ru.leushinilya.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.cells.ItemsAdapter;

public class BudgetFragment extends Fragment implements View.OnClickListener {

    private final int REQUEST_CODE = 1;

    RecyclerView itemsView;
    FloatingActionButton addFab;
    ItemsAdapter itemsAdapter = new ItemsAdapter();
    private int currentPosition;
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
        addFab = view.findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);
        configureRecyclerView();
        itemsAdapter.setData(itemList);
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
    public void onClick(View v) {
        if (v.getId() == R.id.add_fab) {
            Intent intent = new Intent(getActivity(), AddItemActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String name = data.getStringExtra("name");
            String price = data.getStringExtra("price");
            itemList.add(new Item(name, price, currentPosition));
            itemsAdapter.setData(itemList);
        }
    }
}