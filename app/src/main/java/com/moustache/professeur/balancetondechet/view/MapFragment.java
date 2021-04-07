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
import com.moustache.professeur.balancetondechet.model.TrashPin;
import com.moustache.professeur.balancetondechet.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.Console;
import java.util.ArrayList;

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

        addPinPoints();

        return view;
    }

    private void initializeMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        // TODO: use non-deprecated code instead
        map.setBuiltInZoomControls(true);
        // TODO: use GPS-provided coordinates instead
        GeoPoint startPoint = new GeoPoint(43.6178307, 7.0764037);
        mapController = map.getController();
        mapController.setCenter(startPoint);
        mapController.setZoom(18.0);
    }

    private void addPinPoints() {
        ArrayList<OverlayItem> pins = parsePins();
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
                getContext(),
                pins,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return true;
                    }
                }
        );

        map.getOverlays().add(mOverlay);
    }

    private ArrayList<OverlayItem> parsePins() {
        String parsedPins = JsonParser.getJsonFromAssets(getContext(), "pins.json");

        try {
            JSONArray pins = new JSONArray(parsedPins);
            ArrayList<OverlayItem> pinArray = new ArrayList<>(pins.length());

            for (int i = 0; i < pins.length(); i++) {
                JSONObject pinElement = pins.getJSONObject(i);
                TrashPin pin = new TrashPin(pinElement);

                pinArray.add(pin.toOverlayItem());
            }

            return pinArray;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse json");
        }
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
