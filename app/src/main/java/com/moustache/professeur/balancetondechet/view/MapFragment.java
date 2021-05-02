package com.moustache.professeur.balancetondechet.view;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.PinFactory;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.persistance.LoadTrashes;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapFragment extends Fragment {
    private MapView map;
    private IMapController mapController;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<String>();
    private ArrayList<String> permissions = new ArrayList<String>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private ArrayList<Trash> trashes;
    private OverlayItem user;

    //for path drawing
    private ArrayList<Trash> selectedTrashes;
    RoadManager roadManager;
    Road road;
    Road[] mRoads;
    GeoPoint startPoint;
    GeoPoint destinationPoint;
    DirectedLocationOverlay myLocationOverlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedTrashes = bundle.getParcelableArrayList("trashList");
            trashes = bundle.getParcelableArrayList("trashes");

            for (Trash t : trashes) {
                Log.v("trash test", t.toString());
            }
            //https://github.com/MKergall/osmbonuspack
            //https://github.com/MKergall/osmbonuspack/wiki/Tutorial_1

        }
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Configuration.getInstance().load(
                view.getContext(),
                PreferenceManager.getDefaultSharedPreferences(view.getContext())
        );

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTrack = new LocationTrack(view.getContext());

        if (locationTrack.canGetLocation()) {

        } else {
            locationTrack.showSettingsAlert();
        }


        map = view.findViewById(R.id.map);

        initializeMap();
        locationTrack.setLocationChangedCallback((loc) -> {
            try {
                addPinPoints();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        if (selectedTrashes != null && selectedTrashes.size() > 0) {
            //Configuration.getInstance().setUserAgentValue(this.getContext().getPackageName());
            roadManager = new OSRMRoadManager(this.getContext(), Configuration.getInstance().getUserAgentValue());
            Log.v("PATH", "Setting up for path drawing");
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            //waypoints.add(new GeoPoint(locationTrack.getLatitude(), locationTrack.getLongitude()));
            waypoints.add(new GeoPoint(48.13, -1.63));
            //GeoPoint endPoint = new GeoPoint(selectedTrashes.get(0).getTrashPin().getLatitude(), selectedTrashes.get(0).getTrashPin().getLongitude());
            GeoPoint endPoint = new GeoPoint(47.13, -1.63);
            waypoints.add(endPoint);
            try{
                road = roadManager.getRoad(waypoints);
//
            }
            catch(NetworkOnMainThreadException e){
                Log.e("PATH", "could not draw line between points");
                return view;
            }
            ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_CAR);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);
            map.invalidate();
            Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_node);
            TypedArray iconIds = getResources().obtainTypedArray(R.array.direction_icons);
            for (int i = 0; i < road.mNodes.size(); i++) {
                RoadNode node = road.mNodes.get(i);
                Marker nodeMarker = new Marker(map);
                nodeMarker.setPosition(node.mLocation);
                nodeMarker.setIcon(nodeIcon);
                nodeMarker.setTitle("Step " + i);
                map.getOverlays().add(nodeMarker);
                nodeMarker.setSnippet(node.mInstructions);
                nodeMarker.setSubDescription(Road.getLengthDurationText(this.getContext(), node.mLength, node.mDuration));
                //https://developer.mapquest.com/documentation/open/guidance-api/v2/route/get/
//
                int iconId = iconIds.getResourceId(node.mManeuverType, R.drawable.ic_empty);
                if (iconId != R.drawable.ic_empty) {
                    Drawable image = ResourcesCompat.getDrawable(getResources(), iconId, null);
                    nodeMarker.setImage(image);
                }
//
            }

        }

        return view;
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (this.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void initializeMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        // TODO: use non-deprecated code instead
        map.setBuiltInZoomControls(true);
        // TODO: use GPS-provided coordinates instead
        GeoPoint startPoint = new GeoPoint(locationTrack.getLatitude(), locationTrack.getLongitude());
        mapController = map.getController();
        mapController.setCenter(startPoint);
        mapController.setZoom(18.0);

        PinFactory.loadFromContext(getContext());
    }

    private void addPinPoints() throws IOException, ClassNotFoundException {
        map.getOverlays().clear();

        //trashes = parsePins();
        //trashes = LoadTrashes.chargerDechets(MapFragment.this.getContext());

        ArrayList<OverlayItem> pins = trashes
                .stream()
                .map(Trash::getTrashPin)
                .map((pin) -> pin.toOverlayItem(getContext()))
                .collect(Collectors.toCollection(ArrayList::new));

        updateUserPin();
        pins.add(user);

        mapController.setCenter(user.getPoint());

        // This is needed for the onClickListener event below
        MapFragment self = this;

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
                getContext(),
                pins,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        if (index == trashes.size()) {
                            // Have we clicked on the user-position pin?
                            return false;
                        }

                        Trash trash = trashes.get(index);

                        Intent trashDataIntent = new Intent(getContext(), TrashDataActivity.class);
                        trashDataIntent.putExtra("trash", (Parcelable) trash);
                        startActivity(trashDataIntent);

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

    private void updateUserPin() {
        user = PinFactory.userPin(locationTrack.getLatitude(), locationTrack.getLongitude());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }


}

//https://stackoverflow.com/questions/38539637/osmbonuspack-roadmanager-networkonmainthreadexception