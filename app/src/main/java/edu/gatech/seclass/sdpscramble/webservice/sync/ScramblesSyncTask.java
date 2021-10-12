package edu.gatech.seclass.sdpscramble.webservice.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.net.URL;

import edu.gatech.seclass.sdpscramble.BuildConfig;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;

public class ScramblesSyncTask {
    private static final String LOG_TAG = ScramblesSyncTask.class.getSimpleName();

    public static final String JSON_FILE_URL = BuildConfig.BASE_URL + "/.json";

    synchronized public static void syncScrambles(Context context){
        try{
            Log.d(LOG_TAG, "base url: " + BuildConfig.BASE_URL);

            URL scramblesRequestUrl = new URL(JSON_FILE_URL);

            String jsonScramblesResponse = NetworkUtils.getResponseFromHttpUrl(scramblesRequestUrl);

            ContentValues[] scrambleValues = ScramblesJsonUtils.getScrambleContentValuesFromJson(context, jsonScramblesResponse);

            if (scrambleValues != null && scrambleValues.length != 0){
                ContentResolver scrambleContentResolver = context.getContentResolver();

                scrambleContentResolver.delete(ScrambleEntry.CONTENT_URI,
                        null,
                        null);

                scrambleContentResolver.bulkInsert(ScrambleEntry.CONTENT_URI, scrambleValues);
            }

            ContentValues[] playerValues = PlayersJsonUtils.getPlayerContentValuesFromJson(context, jsonScramblesResponse);
            if (playerValues != null && playerValues.length != 0){
                ContentResolver playerContentResolver = context.getContentResolver();

                playerContentResolver.delete(PlayerEntry.CONTENT_URI,
                        null,
                        null);
                playerContentResolver.bulkInsert(PlayerEntry.CONTENT_URI, playerValues);
            }

            ContentValues[] statValues = StatsJsonUtils.getMovieContentValuesFromJson(context, jsonScramblesResponse);
            if (statValues != null && statValues.length != 0 ){
                ContentResolver statContentResolver = context.getContentResolver();

                statContentResolver.delete(StatisticEntry.CONTENT_URI,
                        null,
                        null);

                statContentResolver.bulkInsert(StatisticEntry.CONTENT_URI, statValues);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
