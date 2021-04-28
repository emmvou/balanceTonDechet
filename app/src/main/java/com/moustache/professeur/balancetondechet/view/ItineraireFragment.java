package com.moustache.professeur.balancetondechet.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.TrashAdapter;

public class ItineraireFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraire,container,false);

        ListTrash listTrash = new ListTrash(getContext());
        ListView listView = view.findViewById(R.id.listView);
        TrashAdapter trashAdapter = new TrashAdapter(getContext(), listTrash);
        listView.setAdapter(trashAdapter);

        return view;
    }

    public void onCheckBoxClicked(View view){
        //boolean checked = ((CheckBox) view).isChecked();
        //todo traitement
    }

}
