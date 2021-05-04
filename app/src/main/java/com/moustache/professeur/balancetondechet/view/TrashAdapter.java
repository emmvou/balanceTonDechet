package com.moustache.professeur.balancetondechet.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Filter;
import com.moustache.professeur.balancetondechet.model.ITrashAdapterListener;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;
import com.moustache.professeur.balancetondechet.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TrashAdapter extends BaseAdapter {

    private ListTrash trashes;
    private LayoutInflater inflater;
    private ITrashAdapterListener listener;
    private Pair<Double, Double> location;
    private Boolean[] mChecked;
    private Map<Trash, Double> trashesDist;
    private ItineraireFragment parentFragment;

    public TrashAdapter(Context ctx, ListTrash listTrash, double x, double y, ItineraireFragment fr) {
        parentFragment = fr;
        this.trashes = listTrash;
        this.inflater = LayoutInflater.from(ctx);
        location = Pair.of(x, y);
        mChecked = new Boolean[trashes.size()];
        computeDistances();
        Arrays.fill(mChecked, true);
    }

    public TrashAdapter(Context ctx, ListTrash listTrash, double x, double y, Filter filter, ItineraireFragment fr) {
        this(ctx, listTrash, x, y, fr);
        if (filter.getDistance() >= Filter.EARTH_CIRCUMFERENCE) {
            applyTypeFilter(filter);
        } else {
            applyTypeFilter(filter);
            applyDistFilter(filter.getDistance());
        }
    }

    private void computeDistances() {
        trashesDist = trashes.stream().collect(Collectors.toMap(
                t -> t,
                t -> t.getTrashPin().getDistance(location.getFirst(), location.getSecond())
        ));
        this.trashes.sort(Comparator.comparing(item -> trashesDist.get(item)));
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
        ((TextView) layoutItem.findViewById(R.id.itineraire_type)).setText(trashes.get(position).getType().toString());
        ((ImageView) layoutItem.findViewById(R.id.image)).setImageBitmap(new ImageSaver(layoutItem.getContext())
                .setFileName(trashes.get(position).getImgPath()).setDirectoryName("images").load());


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

        Button jOne = layoutItem.findViewById(R.id.joinOne);
        jOne.setTag(Integer.valueOf(position));
        jOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.goToOneTrash(trashes.get((Integer) v.findViewById(R.id.joinOne).getTag()));
            }
        });

        return layoutItem;
    }

    View.OnClickListener cListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout l = (LinearLayout) v.findViewById(R.id.grower);
            Button jOne = v.findViewById(R.id.joinOne);
            if (l.getVisibility() == View.GONE) {
                l.setVisibility(View.VISIBLE);
                jOne.setVisibility(View.VISIBLE);
            } else {
                l.setVisibility(View.GONE);
                jOne.setVisibility(View.GONE);
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
        trashes = trashes.stream().filter(t -> this.trashesDist.get(t) < dist).collect(Collectors.toCollection(ListTrash::new));
        mChecked = new Boolean[trashes.size()];
        Arrays.fill(mChecked, true);
        computeDistances();
        Log.v("FILTER", trashes.toString());
    }

    public void applyTypeFilter(Filter filter) {
        trashes = trashes.stream().filter(t -> filter.selectedTypes().contains(t.getType())).collect(Collectors.toCollection(ListTrash::new));
        mChecked = new Boolean[trashes.size()];
        Arrays.fill(mChecked, true);
        computeDistances();
        Log.v("FILTER", trashes.toString());
    }

    void removeAllChecks(ViewGroup vg) {
        View v = null;
        for(int i = 0; i < vg.getChildCount(); i++){
            try {
                v = vg.getChildAt(i);
                ((CheckBox)v).setChecked(false);
            }
            catch(Exception e1){ //if not checkBox, null View, etc
                try {
                    removeAllChecks((ViewGroup)v);
                }
                catch(Exception e2){ //v is not a view group
                    continue;
                }
            }
        }
    }

    void addAllChecks(ViewGroup vg) {
        View v = null;
        for(int i = 0; i < vg.getChildCount(); i++){
            try {
                v = vg.getChildAt(i);
                ((CheckBox)v).setChecked(true);
            }
            catch(Exception e1){ //if not checkBox, null View, etc
                try {
                    addAllChecks((ViewGroup)v);
                }
                catch(Exception e2){ //v is not a view group
                    continue;
                }
            }
        }
    }

}

//idées pour ajouter des filtres:
//https://zatackcoder.com/multiple-filters-and-sorting-in-android/
//https://pragmaapps.com/android-multiple-filters/
