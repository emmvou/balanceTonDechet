package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.User;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private User currentUser;
    private TextView currentUserTextView;
    private Button osmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        currentUserTextView = (TextView) findViewById(R.id.currentUserTextView);
        currentUser = getIntent().getExtras().getParcelable("user");
        currentUserTextView.setText(currentUser.getNom());

        osmButton = (Button) findViewById(R.id.OsmButton);
        osmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.OsmButton:
                Log.v("ButtonClick", "Click on osm");

                Intent osmIntent = new Intent(this, OsmActivity.class);
                startActivity(osmIntent);
                break;
            default:
                Log.v("ButtonClick", "Unknown button click");
                break;
        }
    }
}
