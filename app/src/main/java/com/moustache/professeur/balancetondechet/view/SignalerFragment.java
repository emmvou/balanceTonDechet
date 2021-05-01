package com.moustache.professeur.balancetondechet.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.moustache.professeur.balancetondechet.NotificationManager;
import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.NotificationBuilder;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.TrashPin;
import com.moustache.professeur.balancetondechet.model.User;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;
import com.moustache.professeur.balancetondechet.persistance.SaveTrashes;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SignalerFragment extends Fragment {
    private User currentUser;
    private EditText nomTextField;
    private EditText descTextfield;
    private ImageView imageView;
    private Bitmap trashBitmap;
    private Button pictureButton;
    private Button reportButton;
    private static FileWriter fileWriter;
    private ArrayList<Trash> trashes;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<String>();
    private ArrayList<String> permissions = new ArrayList<String>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signaler,container,false);

        reportButton = (Button) view.findViewById(R.id.reportButton);
        pictureButton =(Button) view.findViewById(R.id.pictureButton);
        imageView = (ImageView) view.findViewById(R.id.photo_dechet);

        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.CAMERA },100);
        }

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        Configuration.getInstance().load(
                view.getContext(),
                PreferenceManager.getDefaultSharedPreferences(view.getContext())
        );

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        locationTrack = new LocationTrack(view.getContext());

        if(locationTrack.canGetLocation()){

        }else{
            locationTrack.showSettingsAlert();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[])permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        if (getArguments().getParcelable("User") != null){
           currentUser = getArguments().getParcelable("User");
        }
        Log.v("USER",currentUser.getNom());
        if (getArguments().getParcelableArrayList("trashes")!=null){
            trashes = getArguments().getParcelableArrayList("trashes");
        }

        descTextfield = (EditText) view.findViewById(R.id.descriptionDechet_edit);
        nomTextField = (EditText) view.findViewById(R.id.nomDechet_edit);

        locationTrack.setLocationChangedCallback((location -> {}));

        Button reportButton = (Button) view.findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPath = nomTextField.getText().toString().replace(" ","_")+trashes.size()+".png";
                new ImageSaver(SignalerFragment.this.getContext()).
                        setFileName(imgPath).
                        setDirectoryName("images").
                        save(trashBitmap);

                TrashPin tp = new TrashPin(SignalerFragment.this.locationTrack.getLatitude(),SignalerFragment.this.locationTrack.getLongitude());
                Log.v("DECHET",tp.toString());
                Trash t = new Trash(nomTextField.getText().toString(),descTextfield.getText().toString(),tp,currentUser.getEmail(),imgPath);
                Log.v("DECHET", t.toString());
                JSONObject object = new JSONObject();
                try {
                    object.put("name", nomTextField.getText().toString());
                    object.put("desc", descTextfield.getText().toString());
                    object.put("userEmail",currentUser.getEmail());
                    object.put("isPickedUp", "false");
                    object.put("isApproved", "true");
                    object.put("x", tp.getX());
                    object.put("y", tp.getY());
                    object.put("imgPath",imgPath);
                    trashes.add(t);
                    SaveTrashes.sauvegarderDechets(trashes,view.getContext());
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                Log.v("json",object.toString());

                Context currentContext = getContext();

                new CountDownTimer(30000, 1000)
                {
                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                        Log.d("timer", millisUntilFinished / 1000 + " seconds remaining !");
                    }

                    @Override
                    public void onFinish()
                    {
                        new NotificationBuilder().sendNotificationOnChannel(
                                "Déchet ramassé",
                                "Une personne a ramassé le déchet que vous avez précédemment signalé !",
                                NotificationManager.CHANNEL_2,
                                R.drawable.trash,
                                NotificationCompat.PRIORITY_DEFAULT,
                                currentContext);
                    }
                }.start();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data.getExtras() != null){
            this.trashBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(trashBitmap);
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

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


}
