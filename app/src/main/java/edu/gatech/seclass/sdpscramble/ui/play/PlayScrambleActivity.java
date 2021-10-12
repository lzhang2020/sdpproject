package edu.gatech.seclass.sdpscramble.ui.play;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.gatech.seclass.sdpscramble.R;

public class PlayScrambleActivity extends AppCompatActivity {

    private String mScrambleUriStr = null;
    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_scramble);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCurrentUser = bundle.getString("current_user");
        mScrambleUriStr = bundle.getString("uri");

        PlayScrambleFragment playerScrambleFragment = new PlayScrambleFragment();
        playerScrambleFragment.setArguments(bundle);

        if (mScrambleUriStr != null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.play_scramble_fragment_container, playerScrambleFragment)
                    .commit();
        }
    }
}
