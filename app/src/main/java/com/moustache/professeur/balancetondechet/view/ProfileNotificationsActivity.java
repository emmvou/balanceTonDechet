package com.moustache.professeur.balancetondechet.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

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

        ((TextView) findViewById(R.id.prenom)).setText(currentUser.getPrenom());
        ((TextView) findViewById(R.id.nom)).setText(currentUser.getNom());
        ((TextView) findViewById(R.id.email)).setText(currentUser.getEmail());

        ((CheckBox) findViewById(R.id.checkbox_wants_notifications)).setChecked(currentUser.getWantsToBeNotified() != 0);
        ((EditText) findViewById(R.id.trigger_distance)).setText(Integer.toString(currentUser.getMetersFromTrashToTriggerNotification()));

        switch (currentUser.getNotificationImportanceLevel())
        {
            case 0:
                ((RadioGroup) findViewById(R.id.group_priority)).check(R.id.low_priority);
                break;
            case 1:
                ((RadioGroup) findViewById(R.id.group_priority)).check(R.id.default_priority);
                break;
            case 2:
                ((RadioGroup) findViewById(R.id.group_priority)).check(R.id.high_priority);
                break;
        }

        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setResult(1, new Intent().putExtra("user", (Parcelable) currentUser));
                finish();
            }
        });

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("profile", "cliqu?? !");

                String triggerDistance = ((EditText) findViewById(R.id.trigger_distance)).getText().toString();
                boolean isCheckBoxChecked = ((CheckBox) findViewById(R.id.checkbox_wants_notifications)).isChecked();

                currentUser.setWantsToBeNotified(isCheckBoxChecked ? 1 : 0);
                currentUser.setMetersFromTrashToTriggerNotification(triggerDistance.length() > 0 ? Integer.parseInt(triggerDistance) : 0);

                switch(((RadioGroup) findViewById(R.id.group_priority)).getCheckedRadioButtonId())
                {
                    case R.id.low_priority:
                        currentUser.setNotificationImportanceLevel(0);
                        break;

                    case R.id.default_priority:
                        currentUser.setNotificationImportanceLevel(1);
                        break;

                    case R.id.high_priority:
                        currentUser.setNotificationImportanceLevel(2);
                        break;
                }

                Log.d("user in profile activity", currentUser.toString());

                Intent intent = new Intent();
                intent.putExtra("user", (Parcelable) currentUser);
                setResult(0, intent);

                finish();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Intent intent = new Intent();
        intent.putExtra("user", (Parcelable) currentUser);
        setResult(1, intent);
    }
}
