package ru.leushinilya.loftmoney.cells;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.leushinilya.loftmoney.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MoneyViewHolder>{

    private List<Item> itemList = new ArrayList<>();
    private ItemAdapterClick itemAdapterClick;

    public void setItemAdapterClick(ItemAdapterClick itemAdapterClick) {
        this.itemAdapterClick = itemAdapterClick;
    }

    public void setData(List<Item> items){
        itemList = items;
        notifyDataSetChanged();
    }

    public void updateItem(Item item){
        notifyItemChanged(itemList.indexOf(item));
    }

    @Override
    public MoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MoneyViewHolder(layoutInflater.inflate(R.layout.cell_money, parent, false), itemAdapterClick);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.MoneyViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView, priceTextView;
        private ItemAdapterClick itemAdapterClick;

        public MoneyViewHolder(View itemView, ItemAdapterClick itemAdapterClick) {
            super(itemView);
            this.itemAdapterClick = itemAdapterClick;
            nameTextView = itemView.findViewById(R.id.moneyCellNameView);
            priceTextView = itemView.findViewById(R.id.moneyCellPriceView);
        }

        public void bind(Item item){
            nameTextView.setText(item.getName());
            priceTextView.setText(item.getPrice());
            if(item.getType()==1){
                priceTextView.setTextColor(itemView.getResources().getColor(R.color.apple_green));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapterClick.onItemClick(item);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemAdapterClick.onLongItemClick(item);
                    return true;
                }
            });
            itemView.setBackgroundColor(item.isSelected()?
                    itemView.getResources().getColor(R.color.selection_item_color):
                    itemView.getResources().getColor(R.color.white));
        }
    }
}
