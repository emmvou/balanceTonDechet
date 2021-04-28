package com.moustache.professeur.balancetondechet.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moustache.professeur.balancetondechet.R;

public class TrashAdapter extends BaseAdapter {

    private ListTrash trashes;
    private LayoutInflater inflater;

    public TrashAdapter(Context ctx, ListTrash listTrash) {
        this.trashes = listTrash;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return trashes.size();
    }

    @Override
    public Object getItem(int position) {
        return trashes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;
        layoutItem = (LinearLayout) (convertView==null ? inflater.inflate(R.layout.adapter_item, parent, false) : convertView);
        ((TextView)layoutItem.findViewById(R.id.trashName)).setText(trashes.get(position).getName());
        return layoutItem;
    }
}
