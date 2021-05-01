package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.moustache.professeur.balancetondechet.NotificationManager;
import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.ListTrash;
import com.moustache.professeur.balancetondechet.model.NotificationBuilder;
import com.moustache.professeur.balancetondechet.model.TrashPin;
import com.moustache.professeur.balancetondechet.model.User;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private User currentUser;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        currentUser = getIntent().getExtras().getParcelable("user");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MapFragment()).commit();

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.configureLocationtrack();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            bundle.putParcelable("User",MainMenuActivity.this.currentUser);

            switch (item.getItemId()){
                case R.id.nav_carte:
                    selectedFragment = new MapFragment();
                    break;
                case R.id.nav_itineraire:
                    selectedFragment = new ItineraireFragment();
                    break;
                case R.id.nav_signaler :
                    selectedFragment = new SignalerFragment();
                    break;
            }

            selectedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.drawer_logout :
                Log.v("Drawer","LOGOUT");
                break;
            case R.id.drawer_profil:
                Log.v("Drawer","PROFIL");
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                intentProfile.putExtra("user",currentUser);
                startActivity(intentProfile);
                break;
            case R.id.drawer_notifs:
                Intent intent = new Intent(getApplicationContext(), ProfileNotificationsActivity.class);
                intent.putExtra("user", currentUser);
                startActivityForResult(intent, 0);
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // CONFIG //

    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureLocationtrack()
    {
        locationTrack = new LocationTrack(getApplicationContext());

        locationTrack.setLocationChangedCallback(location ->
        {
            Log.d("MAIN ACTIVITY", "location updated !");

            if(currentUser.getWantsToBeNotified() != 0)
            {
                ListTrash listTrash = new ListTrash(getApplicationContext());

                final double[] minDistance = {-1};

                listTrash.forEach(trash ->
                {
                    Log.d("TRASH", trash.toString());
                    Log.d("TRASH", "distance: " + trash.getTrashPin().getDistance(location.getLatitude(), location.getLongitude())*1000 + "m");

                    TrashPin trashPin = trash.getTrashPin();
                    double distance = trashPin.getDistance(location.getLatitude(), location.getLongitude())*1000;

                    if(minDistance[0] < 0 || distance < minDistance[0])
                    {
                        minDistance[0] = distance;
                    }
                });

                if(minDistance[0] <= currentUser.getMetersFromTrashToTriggerNotification())
                {
                    String channelId = "";
                    int priority = 0;

                    switch(currentUser.getNotificationImportanceLevel())
                    {
                        case 0:
                            channelId = NotificationManager.CHANNEL_1;
                            priority = NotificationCompat.PRIORITY_LOW;
                            break;
                        case 1:
                            channelId = NotificationManager.CHANNEL_2;
                            priority = NotificationCompat.PRIORITY_DEFAULT;
                            break;
                        case 2:
                            channelId = NotificationManager.CHANNEL_3;
                            priority = NotificationCompat.PRIORITY_HIGH;
                            break;
                    }

                    new NotificationBuilder().sendNotificationOnChannel(
                            "Déchet à proximité",
                            "Un déchet se trouve à " + ((int) minDistance[0]) + "m de vous !",
                            channelId,
                            R.drawable.trash,
                            priority,
                            getApplicationContext());
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("user in main activity", currentUser.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("user received", data.getExtras().getParcelable("user").toString());

        if(requestCode == 0)
        {
            User user = data.getExtras().getParcelable("user");
            currentUser = user;
        }
    }
}
