package com.moustache.professeur.balancetondechet.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.utils.Pair;

import java.util.Locale;

public class TrashAdapter extends BaseAdapter {

    private ListTrash trashes;
    private LayoutInflater inflater;
    private Pair<Double, Double> location;

    public TrashAdapter(Context ctx, ListTrash listTrash, double x, double y) {
        this.trashes = listTrash;
        this.inflater = LayoutInflater.from(ctx);
        location = Pair.of(x, y);
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
        ((TextView)layoutItem.findViewById(R.id.distance)).setText(String.format(Locale.FRANCE,"%1.1f km",
                trashes.get(position).getTrashPin().getDistance(location.getFirst(),location.getSecond())));
        return layoutItem;
    }
}
