package com.moustache.professeur.balancetondechet.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moustache.professeur.balancetondechet.R;

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
    private static final String NAME_PARAM = "name";

    private static final String TWITTER_LINK_BASE_URL = "https://mobile.twitter.com/home?status=";
    private static final String MESSAGE_TEXT = "Super%20!%20Je%20viens%20de%20ramasser%20un%20déchet%20dans%20ma%20ville%20grace%20à%20Balance%20Ton%20Déchet.%20cc%20@balanceDechet\n";

    private String name;
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

        args.putString(NAME_PARAM, name);

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
            name = getArguments().getString(NAME_PARAM);
        }

        tweetButton = view.findViewById(R.id.tweet_button);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hijkl", "mnop");
                Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getTwitterLink()));
                startActivity(twitterBrowserIntent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private String getTwitterLink() {
        return new StringBuilder().append(TWITTER_LINK_BASE_URL).append(MESSAGE_TEXT).toString();
    }
}