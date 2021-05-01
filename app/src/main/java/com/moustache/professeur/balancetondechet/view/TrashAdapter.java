package com.moustache.professeur.balancetondechet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Filter;
import com.moustache.professeur.balancetondechet.model.ITrashAdapterListener;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;
import com.moustache.professeur.balancetondechet.utils.Pair;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class TrashAdapter extends BaseAdapter {

    private ListTrash trashes;
    private LayoutInflater inflater;
    private ITrashAdapterListener listener;
    private Pair<Double, Double> location;
    private Boolean[] mChecked;
    private double[] distance;

    public TrashAdapter(Context ctx, ListTrash listTrash, double x, double y) {
        this.trashes = listTrash;
        this.inflater = LayoutInflater.from(ctx);
        location = Pair.of(x, y);
        mChecked = new Boolean[trashes.size()];
        distance = new double[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            distance[i] = trashes.get(i).getTrashPin().getDistance(location.getFirst(), location.getSecond());
        }
        Arrays.fill(mChecked, true);
    }

    public TrashAdapter(Context ctx, ListTrash listTrash, double x, double y, Filter filter) {
        this.trashes = listTrash;
        this.inflater = LayoutInflater.from(ctx);
        location = Pair.of(x, y);
        distance = new double[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            distance[i] = trashes.get(i).getTrashPin().getDistance(location.getFirst(), location.getSecond());
        }
        if (filter.getDistance() >= Filter.EARTH_CIRCUMFERENCE) {

            mChecked = new Boolean[trashes.size()];
            Arrays.fill(mChecked, true);
        } else {
            applyDistFilter(filter.getDistance());
        }
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
        layoutItem = (LinearLayout) (convertView == null ? inflater.inflate(R.layout.adapter_item, parent, false) : convertView);
        ((TextView) layoutItem.findViewById(R.id.trashName)).setText(trashes.get(position).getName());
        ((TextView) layoutItem.findViewById(R.id.desc)).setText(trashes.get(position).getDesc());
        //((ImageView) layoutItem.findViewById(R.id.image)).setImageBitmap(new ImageSaver(layoutItem.getContext())
        //                .setFileName(trashes.get(position).getImgPath()).setDirectoryName("images").load());


        //todo ((ImageView) layoutItem.findViewById(R.id.image)).set(trashes.get(position).get());
        ((TextView) layoutItem.findViewById(R.id.distance)).setText(String.format(Locale.FRANCE, "%1.1f km",
                trashes.get(position).getTrashPin().getDistance(location.getFirst(), location.getSecond())));
        if (convertView == null) {
            ((CheckBox) layoutItem.findViewById(R.id.toPick)).setChecked(true);
        }
        CheckBox cBox = (CheckBox) layoutItem.findViewById(R.id.toPick);
        //https://stackoverflow.com/questions/12647001/listview-with-custom-adapter-containing-checkboxes
        //CheckBox cbox = (CheckBox)layoutItem.findViewById(R.id.toPick);
        cBox.setTag(Integer.valueOf(position));
        cBox.setChecked(mChecked[position]);
        cBox.setOnCheckedChangeListener(mListener);

        layoutItem.setOnClickListener(cListener);

        //layoutItem.findViewById(R.id.toPick).setOnClickListener(click -> {
        //    listener.onClickTrash(trashes.get(position), true);
        //});
        return layoutItem;
    }

    View.OnClickListener cListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout l = (LinearLayout) v.findViewById(R.id.grower);
            if (l.getVisibility() == View.GONE) {
                l.setVisibility(View.VISIBLE);
            } else {
                l.setVisibility(View.GONE);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mChecked[(Integer) buttonView.getTag()] = isChecked; // get the tag so we know the row and store the status
            if (isChecked) {
                trashes.get((Integer) buttonView.getTag());
                Log.v("CHECKBOX", trashes.get((Integer) buttonView.getTag()).getName() + " checked");
            } else {
                Log.v("CHECKBOX", trashes.get((Integer) buttonView.getTag()).getName() + " unchecked");
            }
        }
    };

    public ListTrash getCheckedTrashes() {
        return trashes.stream().filter(t -> mChecked[trashes.indexOf(t)]).collect(Collectors.toCollection(ListTrash::new));
    }

    public void applyDistFilter(double dist) {
        trashes = trashes.stream().filter(t -> distance[trashes.indexOf(t)] < dist).collect(Collectors.toCollection(ListTrash::new));
        mChecked = new Boolean[trashes.size()];
        distance = new double[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            distance[i] = trashes.get(i).getTrashPin().getDistance(location.getFirst(), location.getSecond());
        }
        Arrays.fill(mChecked, true);
        Log.v("FILTER", trashes.toString());
    }

}

//idées pour ajouter des filtres:
//https://zatackcoder.com/multiple-filters-and-sorting-in-android/
//https://pragmaapps.com/android-multiple-filters/
