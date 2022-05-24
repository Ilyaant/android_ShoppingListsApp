package com.example.shoppinglistapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shoppinglistapp.R;
import com.example.shoppinglistapp.SListCreatorActivity;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {
    List<String> list;
    Context context;

    public ListViewAdapter(Context context, List<String> items) {
        super(context, R.layout.list_row, items);
        this.context = context;
        list = items;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row, null);

            TextView number = convertView.findViewById(R.id.number);
            number.setText(position + 1 + ".");

            TextView name = convertView.findViewById(R.id.name);
            name.setText(list.get(position));

            ImageView remove = convertView.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SListCreatorActivity.removeItem(position);
                }
            });
        }
        return convertView;
    }
}
