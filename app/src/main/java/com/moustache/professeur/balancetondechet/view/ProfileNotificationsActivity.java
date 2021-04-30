package com.moustache.professeur.balancetondechet.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.User;

public class ProfileNotificationsActivity extends Activity
{

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_notifications);
        currentUser = getIntent().getExtras().getParcelable("user");
        Log.d("user", currentUser.toString());
    }
}
