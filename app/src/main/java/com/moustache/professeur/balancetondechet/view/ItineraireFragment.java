package com.moustache.professeur.balancetondechet.view;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Filter;
import com.moustache.professeur.balancetondechet.model.FollowedTrashes;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.TrashPin;
import com.moustache.professeur.balancetondechet.model.Trashes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ItineraireFragment extends Fragment {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<String>();
    private ArrayList<String> permissions = new ArrayList<String>();
    private ListView listView;
    private CheckBox checkAll;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText text;
    private Button applyFilters, resetFilters;
    private Filter filter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraire, container, false);
        if (filter != null) {
            Log.v("FILTER", String.valueOf(filter.getDistance()));
        }

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
        ListTrash listTrash = getAllowedTrashes(new ListTrash(Trashes.getInstance().getTrashes()));
        if (listTrash.size() == 0) {
            listView = view.findViewById(R.id.listView);
            initAdapter(listView, new TrashAdapter(getContext(), listTrash, locationTrack.getLatitude(), locationTrack.getLongitude(), ItineraireFragment.this));
            locationTrack.setLocationChangedCallback((loc) -> initAdapter(listView, new TrashAdapter(getContext(), getAllowedTrashes(new ListTrash(Trashes.getInstance().getTrashes())), loc.getLatitude(), loc.getLongitude(), ItineraireFragment.this)));

            FloatingActionButton more = view.findViewById(R.id.more);
            more.setOnClickListener(v -> {

            });

            return view;
        }
        listView = view.findViewById(R.id.listView);
        initAdapter(listView, new TrashAdapter(getContext(), listTrash, locationTrack.getLatitude(), locationTrack.getLongitude(), ItineraireFragment.this));
        locationTrack.setLocationChangedCallback((loc) -> initAdapter(listView, new TrashAdapter(getContext(), getAllowedTrashes(new ListTrash(Trashes.getInstance().getTrashes())), loc.getLatitude(), loc.getLongitude(), ItineraireFragment.this)));

        FloatingActionButton more = view.findViewById(R.id.more);
        more.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(view.getContext(), v);
            popup.setOnMenuItemClickListener(popListener);
            popup.inflate(R.menu.menu_more);
            popup.show();
        });

        checkAll = (CheckBox) view.findViewById(R.id.checkall);
        checkAll.setOnCheckedChangeListener(allListener);

        return view;
    }

    CompoundButton.OnCheckedChangeListener allListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            unorcheckAll(isChecked);
        }
    };

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

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void initAdapter(ListView listView, TrashAdapter trashAdapter) {
        listView.setAdapter(trashAdapter);
    }


    PopupMenu.OnMenuItemClickListener popListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.filter:
                    //alors là on appelle la page pour appliquer les filtres
                    filters();
                    //updateTrashList();
                    return true;
                case R.id.reachall:
                    //là on active la carte
                    Bundle bundle = new Bundle();
                    FollowedTrashes.getInstance().replace(((TrashAdapter) listView.getAdapter()).getCheckedTrashes().getArrayList());
                    //bundle.putParcelableArrayList("trashList", ((TrashAdapter) listView.getAdapter()).getCheckedTrashes().getArrayList());
                    bundle.putParcelableArrayList("trashes", ((TrashAdapter) listView.getAdapter()).getCheckedTrashes().getArrayList());
                    Fragment fragment = new MapFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                    if (filter != null) {
                        Log.v("FILTER", String.valueOf(filter.getDistance()));
                    }
                    return true;
                case R.id.export:
                    //et là on copie les positions vers le presse-papier
                    ListTrash trashes = ((TrashAdapter) listView.getAdapter()).getCheckedTrashes();
                    StringBuilder data = new StringBuilder();
                    data.append("Latitude,Longitude");
                    for (int i = 0; i < trashes.size(); i++) {
                        TrashPin t = trashes.get(i).getTrashPin();
                        data.append("\n" + t.getX() + "," + t.getY());
                    }

                    ClipboardManager clipboard = (ClipboardManager) ItineraireFragment.this.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Coordonnées", data.toString());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(ItineraireFragment.this.getContext(), "Copié", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }
    };

    public void filters() {
        //https://www.youtube.com/watch?v=4GYKOzgQDWI
        dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.filters, null);
        text = (EditText) contactPopupView.findViewById(R.id.distance);
        CheckBox cPortable = contactPopupView.findViewById(R.id.portable);
        CheckBox cEncombrant = contactPopupView.findViewById(R.id.encombrant);
        CheckBox cTresEncombrant = contactPopupView.findViewById(R.id.tresencombran);

        applyFilters = (Button) contactPopupView.findViewById(R.id.filter);
        resetFilters = (Button) contactPopupView.findViewById(R.id.reset);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        applyFilters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //define save button
                try {
                    Log.v("FILTER", "clicked on filtrer avec " + text.getText().toString());
                    filter = new Filter(Integer.parseInt(text.getText().toString()), cPortable.isChecked(), cEncombrant.isChecked(), cTresEncombrant.isChecked());
                } catch (Exception e) {
                    filter = new Filter(cPortable.isChecked(), cEncombrant.isChecked(), cTresEncombrant.isChecked());
                }
                Log.v("FILTER", filter.toString());
                updateTrashList();
                dialog.dismiss();
            }
        });

        resetFilters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //define reset button
                Log.v("FILTER", "clicked on cancel");
                filter = new Filter();
                updateTrashList();
                dialog.dismiss();
            }
        });
    }

    private void updateTrashList() {
        listView.setAdapter(null);
        ListTrash listTrash = getAllowedTrashes(new ListTrash(Trashes.getInstance().getTrashes()));
        if (listTrash.size() == 0) {
            return;
        }

        listView.setAdapter(new TrashAdapter(getContext(), listTrash, locationTrack.getLatitude(), locationTrack.getLongitude(), filter, ItineraireFragment.this));
    }

    void unorcheckAll(boolean check) {
        if (check) {
            ((TrashAdapter) listView.getAdapter()).addAllChecks(listView);
        }
        else{
            ((TrashAdapter) listView.getAdapter()).removeAllChecks(listView);
        }
    }

    private ListTrash getAllowedTrashes(ListTrash lst) {
        return lst.stream().filter(trash -> trash.isApproved() && !trash.isPickedUp()).collect(Collectors.toCollection(ListTrash::new));
    }

    void goToOneTrash(Trash trash) {
        Log.v("PATH", "following : " + trash.toString());
        ArrayList<Trash> lst = new ArrayList<>();
        lst.add(trash);
        Bundle bundle = new Bundle();
        FollowedTrashes.getInstance().replace(new ArrayList<>(Collections.singletonList(trash)));
        bundle.putParcelableArrayList("trashes", lst);
        Fragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        if (filter != null) {
            Log.v("FILTER", String.valueOf(filter.getDistance()));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}
