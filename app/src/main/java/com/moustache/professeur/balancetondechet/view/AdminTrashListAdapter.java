package com.moustache.professeur.balancetondechet.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;

public class AdminTrashListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] names;
    private final String[] descs;
    private final String[] images;

    public AdminTrashListAdapter(Activity context, String[] names, String[] descs, String[] images) {
        super(context, R.layout.trash_item_list, names);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.names=names;
        this.descs=descs;
        this.images=images;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.trash_item_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.desc);

        titleText.setText(names[position]);
        imageView.setImageBitmap(
                new ImageSaver(this.context).
                        setFileName(images[position]).
                        setDirectoryName("images").
                        load());

        subtitleText.setText(descs[position]);

        return rowView;

    };

}
