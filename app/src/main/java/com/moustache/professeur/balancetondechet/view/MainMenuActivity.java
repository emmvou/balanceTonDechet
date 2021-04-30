package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.User;
import com.moustache.professeur.balancetondechet.persistance.LoadTrashes;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private User currentUser;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ArrayList<Trash> trashes = new ArrayList<Trash>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        currentUser = getIntent().getExtras().getParcelable("user");

        try {
            trashes = LoadTrashes.chargerDechets(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Trash t : trashes){
            Log.v("LIST",t.toString());
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

}
