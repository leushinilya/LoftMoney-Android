package ru.leushinilya.loftmoney.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.leushinilya.loftmoney.screens.addItem.AddItemActivity;
import ru.leushinilya.loftmoney.screens.main.diagram.DiagramFragment;
import ru.leushinilya.loftmoney.screens.main.budget.BudgetFragment;
import ru.leushinilya.loftmoney.R;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    TabLayout tabs;
    ViewPager2 pages;
    FloatingActionButton addFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        pages = findViewById(R.id.pages);
        addFAB = findViewById(R.id.add_fab);

//        connect pages and fragments
        pages.setAdapter(new MainPagerAdapter(this));
        pages.setOffscreenPageLimit(3);

//        connect tabs and pages
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabs, pages, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.main_pager_titles)[position]);
            }
        });
        tabLayoutMediator.attach();

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                int currentPosition = tabs.getSelectedTabPosition();
                intent.putExtra("currentPosition", currentPosition);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BudgetFragment) {
                ((BudgetFragment) fragment).compositeDisposable.dispose();
            }
        }
    }

    private class MainPagerAdapter extends FragmentStateAdapter {

        public MainPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            if (position == 2){
                return new DiagramFragment();
            }
            else {
                BudgetFragment budgetFragment = BudgetFragment.newInstance(position);
                return budgetFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}