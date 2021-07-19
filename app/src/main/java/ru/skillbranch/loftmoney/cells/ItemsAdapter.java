package ru.skillbranch.loftmoney.cells;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.skillbranch.loftmoney.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MoneyViewHolder>{

    private List<MoneyItem> moneyItemList = new ArrayList<>();

    public void setData(List<MoneyItem> moneyItems){
        moneyItemList.clear();
        moneyItemList = moneyItems;
        notifyDataSetChanged();
    }

    @Override
    public MoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MoneyViewHolder(layoutInflater.inflate(R.layout.cell_money, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.MoneyViewHolder holder, int position) {
        holder.bind(moneyItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return moneyItemList.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView, volumeTextView;

        public MoneyViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.moneyCellTitleView);
            volumeTextView = itemView.findViewById(R.id.moneyCellVolumeView);
        }

        public void bind(MoneyItem moneyItem){
            titleTextView.setText(moneyItem.getTitle());
            volumeTextView.setText(moneyItem.getValue());
        }
    };
}
