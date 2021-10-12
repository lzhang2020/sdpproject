package edu.gatech.seclass.sdpscramble.ui.stats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.gatech.seclass.sdpscramble.R;

public class PlayerStatActivity extends AppCompatActivity {
    private static final String LOG_TAG = PlayerStatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //String loader = bundle.getString("loader");
        String user = bundle.getString("current_user");

        //Log.d(LOG_TAG, "loader: " + loader);
        Log.d(LOG_TAG, "current user: " + user);

        Bundle statBundle = new Bundle();
        //statBundle.putString("loader", loader);
        statBundle.putString("current_user", user);

        PlayerStatFragment playerStatFragment = new PlayerStatFragment();
        playerStatFragment.setArguments(statBundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.player_stat_fragment_container,
                playerStatFragment).commit();

    }
}
