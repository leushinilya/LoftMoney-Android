package ru.skillbranch.loftmoney;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager2 pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        pages = findViewById(R.id.pages);

//        connect pages and fragments
        pages.setAdapter(new MainPagerAdapter(this));

//        connect tabs and pages
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabs, pages, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.main_pager_titles)[position]);
            }
        });
        tabLayoutMediator.attach();

    }

    private class MainPagerAdapter extends FragmentStateAdapter {

        String[] types = {"expense", "income"};

        public MainPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            if (position == 2) return new BalanceFragment();
            else {
                BudgetFragment budgetFragment = new BudgetFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", types[position]);

                if (getIntent().hasExtra("incomesItemList") && position == 1) {
                    bundle.putSerializable("incomesItemList", getIntent().getExtras().getSerializable("incomesItemList"));
                }

                if (getIntent().hasExtra("expensesItemList") && position == 0) {
                    bundle.putSerializable("expensesItemList", getIntent().getExtras().getSerializable("expensesItemList"));
                }

                budgetFragment.setArguments(bundle);
                return budgetFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}