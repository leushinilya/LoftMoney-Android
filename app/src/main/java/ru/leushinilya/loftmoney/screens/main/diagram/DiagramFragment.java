package ru.leushinilya.loftmoney.screens.main.diagram;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.leushinilya.loftmoney.LoftApp;
import ru.leushinilya.loftmoney.R;
import ru.leushinilya.loftmoney.screens.main.budget.BudgetViewModel;

public class DiagramFragment extends Fragment {

    BudgetViewModel budgetViewModel;

    TextView balanceTextView;
    TextView expensesTextView;
    TextView incomesTextView;
    DiagramView diagramView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViews();
        configureViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        budgetViewModel.updateListFromInternet(
                ((LoftApp) getActivity().getApplication()).itemsAPI,
                0,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0));
        getActivity().findViewById(R.id.add_fab).setVisibility(View.GONE);
        budgetViewModel.updateListFromInternet(
                ((LoftApp) getActivity().getApplication()).itemsAPI,
                1,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0));
        getActivity().findViewById(R.id.add_fab).setVisibility(View.GONE);
    }

    private void configureViewModel() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetViewModel.expensesSum.observe(getViewLifecycleOwner(), expenses -> {
            expensesTextView.setText("" + expenses);
            diagramView.setExpenses(expenses);
        });
        budgetViewModel.incomesSum.observe(getViewLifecycleOwner(), incomes -> {
            incomesTextView.setText("" + incomes);
            diagramView.setIncomes(incomes);
        });
    }

    private void configureViews() {
        balanceTextView = getView().findViewById(R.id.balanceTextView);
        expensesTextView = getView().findViewById(R.id.expensesTextView);
        incomesTextView = getView().findViewById(R.id.incomesTextView);
        diagramView = getView().findViewById(R.id.balanceView);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                balanceTextView.setText(""
                        + (Float.parseFloat(incomesTextView.getText().toString())
                        - Float.parseFloat(expensesTextView.getText().toString()))
                );
            }
        };
        incomesTextView.addTextChangedListener(textWatcher);
        expensesTextView.addTextChangedListener(textWatcher);
    }
}