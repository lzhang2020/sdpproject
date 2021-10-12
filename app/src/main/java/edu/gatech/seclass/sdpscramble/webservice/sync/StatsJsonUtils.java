package edu.gatech.seclass.sdpscramble.webservice.sync;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;

public class StatsJsonUtils {
    private static final String LOG_TAG = StatsJsonUtils.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private static final String STAT_NAME = "playername";
    private static final String STAT_SC_IDEN = "scrambleIdentifier";
    private static final String STAT_IDEN = "statIdentifier";
    private static final String STAT_STATUS = "status";

    private boolean DEBUG = true;

    public static ContentValues[] getMovieContentValuesFromJson(Context context, String scrambleJsonStr)
            throws JSONException {

        Log.d(LOG_TAG, "Extracting features from json...");

        JSONObject baseJsonResponse = new JSONObject(scrambleJsonStr);
        JSONObject statObject = baseJsonResponse.getJSONObject("stats");

        Iterator<String> keys = statObject.keys();

        List<ContentValues> contenValueList = new ArrayList<ContentValues>();

        while (keys.hasNext()){
            String key = keys.next();
            JSONObject innerObject = statObject.getJSONObject(key);

            String playername;
            String scrambleiden;
            String statiden;
            int status;

            Log.d(LOG_TAG, "Stat key: " + key);

            playername = innerObject.getString(STAT_NAME);
            scrambleiden = innerObject.getString(STAT_SC_IDEN);
            statiden = innerObject.getString(STAT_IDEN);
            status = innerObject.getInt(STAT_STATUS);

            ContentValues statValues = new ContentValues();

            statValues.put(StatisticEntry.COL_PLAYER, playername);
            statValues.put(StatisticEntry.COL_SCRAMBLE_IDEN,scrambleiden);
            statValues.put(StatisticEntry.COL_STAT_IDEN, statiden);
            statValues.put(StatisticEntry.COL_STATUS, status);


            contenValueList.add(statValues);

        }

        int size = contenValueList.size();
        ContentValues[] statContentValues = new ContentValues[size];

        int i = 0;
        for (ContentValues contentValues : contenValueList){
            statContentValues[i] = contentValues;

            i++;
        }
        return statContentValues;
    }
}
