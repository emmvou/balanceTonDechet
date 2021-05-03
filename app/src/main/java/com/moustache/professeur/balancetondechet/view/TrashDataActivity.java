package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.Trashes;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TrashDataActivity extends AppCompatActivity {
    private int trashId;

    private String trashName;

    private static final String TWITTER_LINK_BASE_URL = "https://mobile.twitter.com/home?status=";
    private static final String MESSAGE_PT1 = "Super ! Je viens de ramasser un(e) ";
    private static final String MESSAGE_PT2 = " dans ma ville grace à Balance Ton Déchet. cc @balanceDechet\n";

    public TrashDataActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_data);

        trashId = getIntent().getIntExtra("trash-id", -1);

        if (trashId == -1) {
            throw new RuntimeException("Failed to retrieve trash id");
        }

        Trash trash = Trashes.getInstance().getTrashes().get(trashId);

        trashName = trash.getName();

        setTextView(R.id.trash_name, trash.getName());
        setTextView(R.id.trash_desc, trash.getDesc());
        setTextView(R.id.trash_type,trash.getType().toString());

        ImageView trashImg = findViewById(R.id.trash_pict);

        Bitmap bitmap = new ImageSaver(getApplicationContext()).
                setFileName(trash.getImgPath()).
                setDirectoryName("images").
                load();

        trashImg.setImageBitmap(bitmap);

        setButton(R.id.tweet_button, v -> {
            Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getTwitterLink()));
            startActivity(twitterBrowserIntent);
        });

        TrashDataActivity self = this;

        setButton(R.id.backbutton, v -> {
            self.finish();
        });

        setButton(R.id.pick_up, v -> {
            Trashes.getInstance().remove(trashId);
            self.finish();
        });
    }

    void setTextView(int id, String content) {
        TextView textView = findViewById(id);
        textView.setText(content);
    }

    void setButton(int id, View.OnClickListener action) {
        findViewById(id).setOnClickListener(action);
    }

    private String getTwitterLink() {
        String fullMessage = new StringBuilder().append(MESSAGE_PT1).append(trashName).append(MESSAGE_PT2).toString();
        String urlEncodedString;

        try {
            urlEncodedString = URLEncoder.encode(fullMessage, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Unsupported encoding");
        }

        Log.d("TrashDataFragment", urlEncodedString);

        return new StringBuilder().append(TWITTER_LINK_BASE_URL).append(urlEncodedString).toString();
    }
}