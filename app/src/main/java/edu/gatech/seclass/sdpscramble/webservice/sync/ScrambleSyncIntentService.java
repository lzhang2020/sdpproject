package edu.gatech.seclass.sdpscramble.webservice.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class ScrambleSyncIntentService extends IntentService {
    private static final String LOG_TAG = ScrambleSyncIntentService.class.getSimpleName();

    public ScrambleSyncIntentService(){
        super("ScrambleSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "before syncScrambles ....");
        ScramblesSyncTask.syncScrambles(this);
    }
}
