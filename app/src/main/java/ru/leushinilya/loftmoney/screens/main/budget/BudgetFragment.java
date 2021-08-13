package ru.leushinilya.loftmoney.screens.main.budget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import ru.leushinilya.loftmoney.LoftApp;
import ru.leushinilya.loftmoney.R;
import ru.leushinilya.loftmoney.cells.Item;
import ru.leushinilya.loftmoney.cells.ItemAdapterClick;
import ru.leushinilya.loftmoney.cells.ItemsAdapter;

public class BudgetFragment extends Fragment {

    public final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerView itemsView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPosition;
    private BudgetViewModel budgetViewModel;
    private ItemsAdapter itemsAdapter = new ItemsAdapter();
    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ImageView iconBack;
    private ImageView iconTrash;
    private TextView toolBarTextView;
    private FloatingActionButton addFAB;

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
        configureViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.add_fab).setVisibility(View.VISIBLE);
        budgetViewModel.updateListFromInternet(
                ((LoftApp) getActivity().getApplication()).itemsAPI,
                currentPosition,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0));
    }

    @Override
    public void onPause() {
        super.onPause();
        budgetViewModel.isEditMode.postValue(false);
        switchColorsForEditMode(false);
    }

    private void configureRecyclerView() {

        itemsView = getView().findViewById(R.id.itemsView);
        itemsView.setAdapter(itemsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        itemsView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemsView.addItemDecoration(divider);
        itemsAdapter.setItemAdapterClick(new ItemAdapterClick() {
            @Override
            public void onItemClick(Item item) {
                if (budgetViewModel.isEditMode.getValue()) {
                    item.setSelected(!item.isSelected());
                    itemsAdapter.updateItem(item);
                    if(checkSelectedCount()>0){
                        toolBarTextView.setText(
                                getResources().getString(R.string.tool_bar_title_selection)
                                        + checkSelectedCount());
                    } else{
                        budgetViewModel.isEditMode.postValue(false);
                        switchColorsForEditMode(false);
                    }
                }
            }

            @Override
            public void onLongItemClick(Item item) {
                if (!budgetViewModel.isEditMode.getValue()) {
                    budgetViewModel.isEditMode.postValue(true);
                    switchColorsForEditMode(true);
                    item.setSelected(true);
                    itemsAdapter.updateItem(item);
                    toolBarTextView.setText(
                            getResources().getString(R.string.tool_bar_title_selection)
                                    + checkSelectedCount());
                }
            }
        });

    }

    private int checkSelectedCount() {
        int selectedCount = 0;
        for (Item item : budgetViewModel.liveDataItems.getValue()) {
            if (item.isSelected()) {
                selectedCount++;
            }
        }
        return selectedCount;
    }

    private void configureViewModel() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetViewModel.liveDataItems.observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                itemsAdapter.setData(items);
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

    private void configureViews() {
        toolbar = getActivity().findViewById(R.id.toolBar);
        tabLayout = getActivity().findViewById(R.id.tabs);
        iconBack = getActivity().findViewById(R.id.iconBack);
        iconTrash = getActivity().findViewById(R.id.iconTrash);
        toolBarTextView = getActivity().findViewById(R.id.toolBarTextView);
        addFAB = getActivity().findViewById(R.id.add_fab);
        iconBack.setOnClickListener(click -> {
            budgetViewModel.isEditMode.postValue(false);
            switchColorsForEditMode(false);
        });
        iconTrash.setOnClickListener(click ->{
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.delete_dialog_title)
                    .setPositiveButton(R.string.delete_dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (Item item: budgetViewModel.liveDataItems.getValue()){
                                if(item.isSelected()){
                                    budgetViewModel.removeItem(item,
                                            ((LoftApp) getActivity().getApplication()).itemsAPI,
                                            getActivity().getSharedPreferences(getString(R.string.app_name), 0));
                                }
                            }
                            budgetViewModel.isEditMode.postValue(false);
                            switchColorsForEditMode(false);
                            budgetViewModel.updateListFromInternet(
                                    ((LoftApp) getActivity().getApplication()).itemsAPI,
                                    currentPosition,
                                    getActivity().getSharedPreferences(getString(R.string.app_name), 0));
                        }
                    })
                    .setNegativeButton(R.string.delete_dialog_no, (dialog, which) -> {})
                    .show();
        });
    }

    private void switchColorsForEditMode(boolean isEditMode) {

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (isEditMode) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.selection_tab_color));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.selection_tab_color));
            window.setStatusBarColor(getResources().getColor(R.color.selection_tab_color));
            tabLayout.setTabTextColors(
                    getResources().getColor(R.color.white),
                    getResources().getColor(R.color.selection_text_color));
            iconBack.setVisibility(View.VISIBLE);
            iconTrash.setVisibility(View.VISIBLE);
            addFAB.setVisibility(View.GONE);

        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.lightish_blue));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.lightish_blue));
            window.setStatusBarColor(getResources().getColor(R.color.lightish_blue));
            tabLayout.setTabTextColors(
                    getResources().getColor(R.color.tabs_text),
                    getResources().getColor(R.color.white));
            toolBarTextView.setText(getResources().getString(R.string.tool_bar_title));
            iconBack.setVisibility(View.GONE);
            iconTrash.setVisibility(View.GONE);
            addFAB.setVisibility(View.VISIBLE);
            for (Item item : budgetViewModel.liveDataItems.getValue()) {
                if (item.isSelected()) item.setSelected(false);
                itemsAdapter.updateItem(item);
            }
        }
    }

}