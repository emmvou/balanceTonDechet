package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.moustache.professeur.balancetondechet.NotificationManager;
import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.FollowedTrashes;
import com.moustache.professeur.balancetondechet.model.LocationTrack;
import com.moustache.professeur.balancetondechet.model.PendingTrashes;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.NotificationBuilder;
import com.moustache.professeur.balancetondechet.model.Trashes;
import com.moustache.professeur.balancetondechet.model.User;
import com.moustache.professeur.balancetondechet.persistance.LoadTrashes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private User currentUser;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ArrayList<Trash> trashes = new ArrayList<Trash>();
    private LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Trashes.getInstance();
        PendingTrashes.getInstance();
        FollowedTrashes.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        currentUser = getIntent().getExtras().getParcelable("user");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.beige));

        try {
            trashes = LoadTrashes.chargerDechets(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Trash t : trashes){
            Log.v("LIST", t.toString());
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("trashes",MainMenuActivity.this.trashes);
        Fragment initFragment = new MapFragment();
        initFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,initFragment).commit();

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
            bundle.putParcelableArrayList("trashes",MainMenuActivity.this.trashes);

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
            case R.id.drawer_logout:
                Log.v("Drawer","LOGOUT");
                Intent intentLogout = new Intent(this, LoginActivity.class);
                startActivity(intentLogout);
                break;
            case R.id.drawer_profil:
                Log.v("Drawer","PROFIL");
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                intentProfile.putExtra("user",(Parcelable)currentUser);
                startActivity(intentProfile);
                break;
            case R.id.drawer_notifs:
                Intent intent = new Intent(getApplicationContext(), ProfileNotificationsActivity.class);
                intent.putExtra("user", (Parcelable) currentUser);
                startActivityForResult(intent, 0);
                break;
            case R.id.drawer_admin:
                Intent intentAdmin = new Intent(getApplicationContext(),AdminActivity.class);
                intentAdmin.putParcelableArrayListExtra("trashes",trashes);
                startActivity(intentAdmin);
                break;
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
        if(currentUser.isAdmin()){
            navigationView.getMenu().getItem(3).setVisible(true);
        }
    }

    private void configureLocationtrack()
    {
        locationTrack = new LocationTrack(getApplicationContext());

        locationTrack.setLocationChangedCallback(location ->
        {
            Log.d("MAIN ACTIVITY", "location updated !");

            if(currentUser.getWantsToBeNotified() != 0)
            {
                double x = location.getLatitude();
                double y = location.getLongitude();
                List<Trash> listTrash = Trashes.getInstance().getTrashes();

                listTrash = listTrash.stream().filter( trash ->
                {
                    return trash.getTrashPin().getDistance(x, y)*1000 <= currentUser.getMetersFromTrashToTriggerNotification();
                }).collect(Collectors.toList());

                if(listTrash.size() > 0)
                {
                    String channelId = "";
                    int priority = 0;
                    String title = listTrash.size() == 1 ? "Déchet à proximité" : "Déchets à proximité";
                    String message = listTrash.size() == 1 ?
                            "Un déchet se trouve à " + (int) (listTrash.get(0).getTrashPin().getDistance(x, y)*1000)  + "m de vous !" :
                            "Plusieurs déchets se trouvent à moins de " + currentUser.getMetersFromTrashToTriggerNotification() + "m de vous !";

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
                            title,
                            message,
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

        if(requestCode == 0 && data != null)
        {
            User user = data.getExtras().getParcelable("user");
            currentUser = user;
        }
    }
}
