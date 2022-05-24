package com.example.shoppinglistapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistapp.Models.SList;
import com.example.shoppinglistapp.R;
import com.example.shoppinglistapp.SListClickListener;

import java.util.List;

public class SListsAdapter extends RecyclerView.Adapter<SListViewHolder> {
    Context context;
    List<SList> list;
    SListClickListener listener;

    public SListsAdapter(Context context, List<SList> list, SListClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SListViewHolder(LayoutInflater.from(context).inflate(R.layout.shopping_lists, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SListViewHolder holder, int position) {
        holder.textView_listStart.setText(list.get(position).getProducts().get(0) + "...");

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        holder.lists_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.lists_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.lists_container);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class SListViewHolder extends RecyclerView.ViewHolder {
    CardView lists_container;
    TextView textView_listStart, textView_date;

    public SListViewHolder(@NonNull View itemView) {
        super(itemView);
        lists_container = itemView.findViewById(R.id.lists_container);
        textView_listStart = itemView.findViewById(R.id.textView_listStart);
        textView_date = itemView.findViewById(R.id.textView_date);
    }
}
