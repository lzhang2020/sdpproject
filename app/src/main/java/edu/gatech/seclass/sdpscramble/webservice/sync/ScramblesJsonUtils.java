package edu.gatech.seclass.sdpscramble.webservice.sync;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;

public class ScramblesJsonUtils {
    private static final String LOG_TAG = ScramblesJsonUtils.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private static final String SC_ANS = "answer";
    private static final String SC_AUTH = "author";
    private static final String SC_CLUE = "clue";
    private static final String SC_SOLVED = "correct";
    private static final String SC_IDEN = "identifier";
    private static final String SC_IS_SOL = "isSolved";
    private static final String SC_PHRASE = "phrase";

    private boolean DEBUG = true;

    public static ContentValues[] getScrambleContentValuesFromJson(Context context, String scrambleJsonStr)
            throws JSONException {

        Log.d(LOG_TAG, "Extracting features from json...");
        Log.d(LOG_TAG, "scrambleJsonStr: " + scrambleJsonStr);

        //create a JSONObject from the JSON response string
        JSONObject baseJsonResponse = new JSONObject(scrambleJsonStr);
        JSONObject scrambleObject = baseJsonResponse.getJSONObject("scrambles");
        Iterator<String> keys = scrambleObject.keys();

        List<ContentValues> contenValueList = new ArrayList<ContentValues>();

        while (keys.hasNext()){
            String key = keys.next();
            JSONObject innerObject = scrambleObject.getJSONObject(key);

            String answer;
            String author;
            String clue;
            String phrase;
            String indentifier;
            int isSolved;
            int correct;

            Log.d(LOG_TAG, "Scramble key: " + key);

            answer = innerObject.getString(SC_ANS);
            author = innerObject.getString(SC_AUTH);
            clue = innerObject.getString(SC_CLUE);
            phrase = innerObject.getString(SC_PHRASE);
            indentifier = innerObject.getString(SC_IDEN);
            isSolved = innerObject.getInt(SC_IS_SOL);
            correct = innerObject.getInt(SC_SOLVED);

            ContentValues scrambleValues = new ContentValues();

            scrambleValues.put(ScrambleEntry.COL_SCRAMBLED, answer);
            scrambleValues.put(ScrambleEntry.COL_AUTHOR, author);
            scrambleValues.put(ScrambleEntry.COL_CLUE, clue);
            scrambleValues.put(ScrambleEntry.COL_PHRASE, phrase);
            scrambleValues.put(ScrambleEntry.COL_IDENTIFIER, indentifier);
            scrambleValues.put(ScrambleEntry.COL_IS_SOLVED, isSolved);
            scrambleValues.put(ScrambleEntry.COL_CORRECT, correct);

            contenValueList.add(scrambleValues);
        }

        int size = contenValueList.size();

        ContentValues[] scrambleContentValues = new ContentValues[size];

        int i = 0;
        for (ContentValues contentValues : contenValueList){
            scrambleContentValues[i] = contentValues;

            i++;
        }

        return scrambleContentValues;
    }
}
