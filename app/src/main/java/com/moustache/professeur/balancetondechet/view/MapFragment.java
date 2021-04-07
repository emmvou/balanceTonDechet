package com.moustache.professeur.balancetondechet.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.moustache.professeur.balancetondechet.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapFragment extends Fragment {
    private MapView map;
    private IMapController mapController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);

        Configuration.getInstance().load(
                view.getContext(),
                PreferenceManager.getDefaultSharedPreferences(view.getContext())
        );

        map = view.findViewById(R.id.map);

        initializeMap();

        return view;
    }

    private void initializeMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        // TODO: use non-deprecated code instead
        map.setBuiltInZoomControls(true);
        // TODO: use GPS-provided coordinates instead
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
        mapController = map.getController();
        mapController.setCenter(startPoint);
        mapController.setZoom(18.0);
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }
}
