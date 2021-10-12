package edu.gatech.seclass.sdpscramble.ui.scrambles;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.ui.create.CreateScrambleActivity;
import edu.gatech.seclass.sdpscramble.ui.play.PlayScrambleActivity;
import edu.gatech.seclass.sdpscramble.ui.scrambles.ScramblesFragment;
import edu.gatech.seclass.sdpscramble.ui.stats.PlayerStatActivity;
import edu.gatech.seclass.sdpscramble.ui.stats.ScrambleStatActivity;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;

import static android.R.attr.id;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class ScramblesActivity extends AppCompatActivity implements ScramblesFragment.Callback {
    private static final String LOG_TAG = ScrambleEntry.class.getSimpleName();

    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrambles);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCurrentUser = bundle.getString("current_user");

        ScramblesFragment scramblesFragment = new ScramblesFragment();
        scramblesFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.scramble_fragment_container,
                scramblesFragment).commit();

    }

    @Override
    public void onItemSelected(Uri scrambleUri){
        Intent scrambleDetailIntent = new Intent(this, PlayScrambleActivity.class);
        scrambleDetailIntent.putExtra("uri", scrambleUri.toString());
        scrambleDetailIntent.putExtra("current_user", mCurrentUser);
        startActivity(scrambleDetailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_create_scramble: {
                Intent startCreateScrambleActivity = new Intent(this, CreateScrambleActivity.class);
                startActivity(startCreateScrambleActivity);
                return true;
            }
            case R.id.menu_resume_scramble:{
                String identifier;
                Cursor userCursor = this.getContentResolver().query(PlayerEntry.CONTENT_URI,
                        ConstantUtils.PLAYER_COLUMNS,
                        PlayerEntry.COL_USERNAME + "=?",
                        new String[]{mCurrentUser},
                        null);
                userCursor.moveToFirst();
                identifier = userCursor.getString(ConstantUtils.COLUMN_PROCESSING_SCRAMBLE);

                Log.d(LOG_TAG, "identifier: " + identifier);

                Cursor scrambleCusor = this.getContentResolver().query(ScrambleEntry.CONTENT_URI,
                        ConstantUtils.SCRAMBLE_COLUMNS,
                        ScrambleEntry.COL_IDENTIFIER + "=?",
                        new String[]{identifier},
                        null);

                if (null == scrambleCusor || scrambleCusor.getCount() == 0){
                    Toast.makeText(this, "You don't have processing scramble.", Toast.LENGTH_SHORT).show();
                    return true;

                } else{
                    scrambleCusor.moveToFirst();

                    int scrambleId = scrambleCusor.getInt(ConstantUtils.COLUMN_SCRAMBLE_ID);
                    Uri scrambleUri = ScrambleEntry.buildScrambleUri(scrambleId);

                    Intent scrambleDetailIntent = new Intent(this, PlayScrambleActivity.class);
                    scrambleDetailIntent.putExtra("uri", scrambleUri.toString());
                    scrambleDetailIntent.putExtra("current_user", mCurrentUser);
                    startActivity(scrambleDetailIntent);
                    return true;
                }
            }
            case R.id.menu_view_stats: {
                Intent startScrambleStatActivity = new Intent(this, ScrambleStatActivity.class);
                //startScrambleStatActivity.putExtra("loader", "all_stats");
                //startScrambleStatActivity.putExtra("current_user", mCurrentUser);
                startActivity(startScrambleStatActivity);
                return true;
            }
            case R.id.menu_player_stats: {
                Intent startPlayerStatActivity = new Intent(this, PlayerStatActivity.class);
                //startPlayerStatActivity.putExtra("loader", "player_stats");
                startPlayerStatActivity.putExtra("current_user", mCurrentUser);
                startActivity(startPlayerStatActivity);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}

