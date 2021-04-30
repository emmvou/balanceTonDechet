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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.TrashPin;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ItineraireFragment extends Fragment {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<String>();
    private ArrayList<String> permissions = new ArrayList<String>();
    private ListView listView;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraire,container,false);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[])permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTrack = new LocationTrack(view.getContext());

        if(locationTrack.canGetLocation()){

        }else{
            locationTrack.showSettingsAlert();
        }

        ListTrash listTrash = new ListTrash(getContext());
        listView = view.findViewById(R.id.listView);
        initAdapter(listView, new TrashAdapter(getContext(), listTrash, locationTrack.getLatitude(), locationTrack.getLongitude()));
        //TrashAdapter trashAdapter = new TrashAdapter(getContext(), listTrash, locationTrack.getLatitude(), locationTrack.getLongitude());
        //listView.setAdapter(trashAdapter);
        //trashAdapter.addListener(this);
        locationTrack.setLocationChangedCallback((loc) -> initAdapter(listView, new TrashAdapter(getContext(), listTrash, loc.getLatitude(), loc.getLongitude())));

        Button more = view.findViewById(R.id.more);
        more.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(view.getContext(), v);
            popup.setOnMenuItemClickListener(popListener);
            popup.inflate(R.menu.menu_more);
            popup.show();
        });

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

    private void initAdapter(ListView listView, TrashAdapter trashAdapter){
        listView.setAdapter(trashAdapter);
    }


    PopupMenu.OnMenuItemClickListener popListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()){
                case R.id.filter:
                    //todo alors là on appelle la page pour appliquer les filtres
                    return true;
                case R.id.reachall:
                    //là on active la carte
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("trashList", ((TrashAdapter)listView.getAdapter()).getCheckedTrashes());
                    Fragment fragment = new MapFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                    return true;
                case R.id.export:
                    //et là on copie les positions vers le presse-papier
                    ListTrash trashes =  ((TrashAdapter)listView.getAdapter()).getCheckedTrashes();
                    StringBuilder data = new StringBuilder();
                    data.append("Latitude,Longitude");
                    for(int i = 0; i < trashes.size(); i++){
                        TrashPin t = trashes.get(i).getTrashPin();
                        data.append("\n"+ t.getX() +","+t.getY());
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




}
