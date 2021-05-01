package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashDataFragment extends Fragment {
    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    */
    private Trash trash;
    private ImageView trashImg;

    private static final String TWITTER_LINK_BASE_URL = "https://mobile.twitter.com/home?status=";
    private static final String MESSAGE_PT1 = "Super ! Je viens de ramasser un(e) ";
    private static final String MESSAGE_PT2 = " dans ma ville grace à Balance Ton Déchet. cc @balanceDechet\n";

    private Button tweetButton;

    public TrashDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name trash name
     * @return A new instance of fragment TrashDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrashDataFragment newInstance(String name) {
        TrashDataFragment fragment = new TrashDataFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trash_data, container, false);

        if (getArguments() != null) {
            trash = getArguments().getParcelable("trash");
            Log.v("SELECTED TRASH", trash.toString());
        }


        trashImg = view.findViewById(R.id.trash_image);

        Bitmap bitmap = new ImageSaver(view.getContext()).
                setFileName(trash.getImgPath()).
                setDirectoryName("images").
                load();

        trashImg.setImageBitmap(bitmap);
        tweetButton = view.findViewById(R.id.tweet_button);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getTwitterLink()));
                startActivity(twitterBrowserIntent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private String getTwitterLink() {
        String fullMessage = new StringBuilder().append(MESSAGE_PT1).append(trash.getName()).append(MESSAGE_PT2).toString();
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