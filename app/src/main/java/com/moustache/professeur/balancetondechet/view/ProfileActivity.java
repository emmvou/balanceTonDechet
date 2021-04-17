package com.moustache.professeur.balancetondechet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.User;

public class ProfileActivity extends AppCompatActivity {
    private User currentUser;

    private TextView prenomTextView;
    private TextView nomTextView;
    private TextView emailTextView;

    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getIntent().getExtras().getParcelable("user");
        setContentView(R.layout.activity_profile);

        // Text views
        prenomTextView = findViewById(R.id.prenom);
        prenomTextView.setText(currentUser.getPrenom());

        nomTextView = findViewById(R.id.nom);
        nomTextView.setText(currentUser.getNom());

        emailTextView = findViewById(R.id.email);
        emailTextView.setText(currentUser.getEmail());

        //Edit texts

        prenomEditText = findViewById(R.id.prenom_edit);
        prenomEditText.setText(currentUser.getPrenom());

        nomEditText = findViewById(R.id.nom_edit);
        nomEditText.setText(currentUser.getNom());

        emailEditText = findViewById(R.id.email_edit);
        emailEditText.setText(currentUser.getEmail());

        ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}