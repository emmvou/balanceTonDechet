package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.User;

public class MainMenuActivity extends AppCompatActivity {
    private User currentUser;
    private TextView currentUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        currentUserTextView = (TextView) findViewById(R.id.currentUserTextView);
        currentUser = getIntent().getExtras().getParcelable("user");
        currentUserTextView.setText(currentUser.getNom());
    }


}
