package ru.skillbranch.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.skillbranch.loftmoney.cells.Item;
import ru.skillbranch.loftmoney.cells.ItemsAdapter;

public class BudgetFragment extends Fragment implements View.OnClickListener {

    RecyclerView itemsView;
    FloatingActionButton addFab;
    ItemsAdapter itemsAdapter = new ItemsAdapter();
    String type;
    ArrayList<Item> itemList = new ArrayList<>();

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
        type = getArguments().getString("type");
        addFab.setOnClickListener(this);
        configureRecyclerView();
        updateList();
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
            intent.putExtra("type", this.type);
            intent.putExtra("itemList", itemList);
            startActivity(intent);
        }
    }

    private void updateList() {
        if (getArguments().getSerializable("incomesItemList") != null && type.equals("income")) {
            itemList = (ArrayList<Item>) getArguments().getSerializable("incomesItemList");
        }

        if (getArguments().getSerializable("expensesItemList") != null && type.equals("expense")) {
            itemList = (ArrayList<Item>) getArguments().getSerializable("expensesItemList");
        }
    }

}