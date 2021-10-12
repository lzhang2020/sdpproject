package edu.gatech.seclass.sdpscramble.webservice.sync;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.models.Scramble;

public class PlayersJsonUtils {
    private static final String LOG_TAG = PlayersJsonUtils.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private static final String PLAY_UN = "userName";
    private static final String PLAY_FN = "firstName";
    private static final String PLAY_LN = "lastName";
    private static final String PLAY_EMAIL = "email";
    private static final String PLAY_LOG = "isLogin";
    private static final String PLAY_CREAT = "createdNumber";
    private static final String PLAY_SOL = "solvedNumber";
    private static final String PLAY_SOL_BY = "solvedByNumber";
    private static final String PLAY_PROCESS = "processing";

    private boolean DEBUG = true;

    public static ContentValues[] getPlayerContentValuesFromJson(Context context, String scrambleJsonStr)
            throws JSONException {

        Log.d(LOG_TAG, "Extracting features from json...");

        //create a JSONObject from the JSON response string
        JSONObject baseJsonResponse = new JSONObject(scrambleJsonStr);
        JSONObject playerObject = baseJsonResponse.getJSONObject("players");
        Iterator<String> keys = playerObject.keys();

        List<ContentValues> contenValueList = new ArrayList<ContentValues>();

        while (keys.hasNext()){
            String key = keys.next();
            JSONObject innerObject = playerObject.getJSONObject(key);

            String username;
            String firstname;
            String lastname;
            String email;
            //int islogin;
            int created;
            int solved;
            int solvedby;
            String process;

            Log.d(LOG_TAG, "Player key: " + key);

            username = innerObject.getString(PLAY_UN);
            firstname = innerObject.getString(PLAY_FN);
            lastname = innerObject.getString(PLAY_LN);
            email = innerObject.getString(PLAY_EMAIL);
            //islogin = innerObject.getInt(PLAY_LOG);
            created = innerObject.getInt(PLAY_CREAT);
            solved = innerObject.getInt(PLAY_SOL);
            solvedby = innerObject.getInt(PLAY_SOL_BY);
            process = innerObject.getString(PLAY_PROCESS);

            ContentValues playerValues = new ContentValues();

            playerValues.put(PlayerEntry.COL_USERNAME, username);
            playerValues.put(PlayerEntry.COL_FIRSTNAME,firstname);
            playerValues.put(PlayerEntry.COL_LASTNAME, lastname);
            playerValues.put(PlayerEntry.COL_EMAIL, email);
            playerValues.put(PlayerEntry.COL_IS_LOGIN, 0);
            playerValues.put(PlayerEntry.COL_CREATED_NUM, created);
            playerValues.put(PlayerEntry.COL_SOLVED_NUMBER, solved);
            playerValues.put(PlayerEntry.COL_SOLVED_BY_NUMBER, solvedby);
            playerValues.put(PlayerEntry.COL_PROCESS_SCRAMBLE, process);

            contenValueList.add(playerValues);

        }

        int size = contenValueList.size();

        ContentValues[] playerContentValues = new ContentValues[size];

        int i = 0;
        for (ContentValues contentValues : contenValueList){
            playerContentValues[i] = contentValues;

            i++;
        }

        return playerContentValues;
    }
}
