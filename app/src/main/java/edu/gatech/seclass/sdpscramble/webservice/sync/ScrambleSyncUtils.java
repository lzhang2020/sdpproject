package edu.gatech.seclass.sdpscramble.webservice.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

public class ScrambleSyncUtils {
    private static final String LOG_TAG = ScrambleSyncUtils.class.getSimpleName();

    public static void startImmediateSync(@NonNull final Context context){
        Log.d(LOG_TAG, "startImmediateSync ...... ");
        Intent intentToSyncImmediately = new Intent(context, ScrambleSyncIntentService.class);
        Log.d(LOG_TAG, "before starting service ......");
        context.startService(intentToSyncImmediately);
    }
}
