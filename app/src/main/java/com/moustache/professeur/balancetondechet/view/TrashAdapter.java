package com.moustache.professeur.balancetondechet.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.ITrashAdapterListener;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.utils.Pair;

import java.util.Arrays;
import java.util.Locale;

public class TrashAdapter extends BaseAdapter {

    private ListTrash trashes;
    private LayoutInflater inflater;
    private ITrashAdapterListener listener;
    private Pair<Double, Double> location;
    private Boolean[] mChecked;

    public TrashAdapter(Context ctx, ListTrash listTrash, double x, double y) {
        this.trashes = listTrash;
        this.inflater = LayoutInflater.from(ctx);
        location = Pair.of(x, y);
        mChecked = new Boolean[trashes.size()];
        Arrays.fill(mChecked, true);
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
        if(convertView == null){

        ((CheckBox)layoutItem.findViewById(R.id.toPick)).setChecked(true);
        }
        CheckBox cBox = (CheckBox)layoutItem.findViewById(R.id.toPick);
        //https://stackoverflow.com/questions/12647001/listview-with-custom-adapter-containing-checkboxes
        //CheckBox cbox = (CheckBox)layoutItem.findViewById(R.id.toPick);
        cBox.setTag(Integer.valueOf(position));
        cBox.setChecked(mChecked[position]);
        cBox.setOnCheckedChangeListener(mListener);

        //layoutItem.findViewById(R.id.toPick).setOnClickListener(click -> {
        //    listener.onClickTrash(trashes.get(position), true);
        //});
        return layoutItem;
    }

    //public void addListener(ITrashAdapterListener listener){
    //    this.listener = listener;
    //}

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mChecked[(Integer)buttonView.getTag()] = isChecked; // get the tag so we know the row and store the status
            Log.v("CHECKBOX", "alors salut, donc là on vient de cliquer sur la checkbox");
        }
    };

    //recuperer la liste de tous les items cochés
}
