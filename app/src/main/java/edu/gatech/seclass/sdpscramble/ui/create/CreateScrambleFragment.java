package edu.gatech.seclass.sdpscramble.ui.create;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.models.Scramble;
import edu.gatech.seclass.sdpscramble.ui.scrambles.ScramblesActivity;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;
import edu.gatech.seclass.sdpscramble.webservice.remote.ExternalWebService;
import edu.gatech.seclass.sdpscramble.webservice.remote.WebServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateScrambleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = CreateScrambleFragment.class.getSimpleName();

    public static final String DEFAULT_PLAYER_URI = "PLAYER_URI";
    public static final String DEFAULT_SCRAMBLE_URI = "SCRAMBLE_URI";
    public static final String DEFAULT_STATS_URI = "STATS_URI";

    private static final int PLAYER_LOADER_ID = 10;
    private static final int SCRAMBLE_LOADER_ID = 20;
    private static final int STATS_LOADER_ID = 20;

    private Cursor mUserCursor;
    private Uri mUri;
    private EditText mPhrase;
    private EditText mClue;
    private TextView mResult;
    private TextView mSubmitResult;
    private Button mScrambleButton;
    private Button mAcceptButton;

    private String mUsername;
    private String mWord;
    private int mCreatedNum;

    private boolean mSuccessCreated = false;

    public CreateScrambleFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(PLAYER_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_scramble_create, container, false);

        mPhrase = (EditText) rootView.findViewById(R.id.phrase_message2);
        mClue = (EditText) rootView.findViewById(R.id.clue_message);
        mResult = (TextView) rootView.findViewById(R.id.scramble_result);
        mSubmitResult = (TextView) rootView.findViewById(R.id.scrambleInsertResult);

        mScrambleButton = (Button) rootView.findViewById(R.id.scramble_button);
        mAcceptButton = (Button) rootView.findViewById(R.id.accept);

        mScrambleButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mSubmitResult.setText(null);
                mWord = mPhrase.getText().toString();

                boolean ab = mWord.matches(".*[a-zA-Z].*");

                if (mWord.isEmpty() || !ab) {
                    //Context context = getApplicationContext();
                    CharSequence text = "Phrase Required";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
                else {
                    mResult.setText(calculate(mWord));
                }
            }
        });

        mAcceptButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String phraseMessage = mPhrase.getText().toString();
                String clueMessage = mClue.getText().toString();
                String scrambledWord = mResult.getText().toString();
                if (scrambledWord == null) {
                    //Context context = getApplicationContext();
                    CharSequence text = "Phrase Required";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                } else {
                    Random rand = new Random();
                    int rand_un = rand.nextInt(9999);

                    String scrambleStr = scrambledWord.replaceAll(" ", "");
                    String initStr = scrambleStr;
                    if (scrambleStr.length() > 4){
                        initStr = scrambleStr.substring(0,4);
                    }
                    String identifier = initStr + Integer.toString(rand_un);

                    Scramble newScramble = new Scramble(scrambledWord,clueMessage,phraseMessage, mUsername, 0, identifier, 0);

                    insertNewScramble(newScramble);


                    WebServiceInterface service = ExternalWebService.getInstance();

                    Call<Scramble> callScramble = service.createScramble(newScramble.getScrambleIdentifier(), newScramble);
                    callScramble.enqueue(new Callback<Scramble>() {
                    @Override
                    public void onResponse(Call<Scramble> call, Response<Scramble> response) {
                        Log.d(LOG_TAG, "success");
                    }

                    @Override
                    public void onFailure(Call<Scramble> call, Throwable t) {
                        Log.d(LOG_TAG, "failed");
                    }
                    });

                    mCreatedNum++;

                    ContentValues playerValues = new ContentValues();
                    playerValues.put(PlayerEntry.COL_CREATED_NUM, mCreatedNum);
                    getActivity().getContentResolver().update(PlayerEntry.CONTENT_URI,
                            playerValues,
                            PlayerEntry.COL_USERNAME + "=?",
                            new String[]{mUsername});

                    Call<Integer> callCreatedNumber = service.setCreatedNumber(mUsername, mCreatedNum);
                    callCreatedNumber.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d(LOG_TAG, "success");
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(LOG_TAG, "failed");
                        }
                    });

                    if (mSuccessCreated){
                        Intent startScramblesActivity = new Intent(getActivity(), ScramblesActivity.class);
                        startScramblesActivity.putExtra("current_user", mUsername);
                        startActivity(startScramblesActivity);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId){
            case PLAYER_LOADER_ID:{
                return new CursorLoader(
                        getActivity(),
                        PlayerEntry.CONTENT_URI,
                        ConstantUtils.PLAYER_COLUMNS,
                        PlayerEntry.COL_IS_LOGIN + " = ?",
                        new String[]{String.valueOf(1)},
                        null
                );
            }

            default:{
                throw new RuntimeException("Loader not implemented: " + loaderId);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mUserCursor = data;
        mUserCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);
        mUsername = mUserCursor.getString(ConstantUtils.COLUMN_USERNAME);
        mCreatedNum = mUserCursor.getInt(ConstantUtils.COLUMN_CREATED_NUM);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUserCursor = null;
    }

    /*
        public void handleClick(View view) {
            switch (view.getId()) {
                case R.id.scramble_button:
                    mSubmitResult.setText(null);
                    mWord = mPhrase.getText().toString();

                    boolean ab = mWord.matches(".*[a-zA-Z].*");

                    if (mWord.isEmpty() || !ab) {
                        //Context context = getApplicationContext();
                        CharSequence text = "Phrase Required";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                    }
                    else {
                        mResult.setText(calculate(mWord));
                    }
                    break;

                case R.id.accept:
                    String phraseMessage = mPhrase.getText().toString();
                    String clueMessage = mClue.getText().toString();
                    String scrambledWord = mResult.getText().toString();
                    if (scrambledWord == null) {
                        //Context context = getApplicationContext();
                        CharSequence text = "Phrase Required";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                    } else {
                        Scramble newScramble = new Scramble(phraseMessage,clueMessage,scrambledWord, mUsername, 0, 0);
                        //ScrambleProvider.insert();
                        //mSubmitResult.setText("Above scramble has been submitted to database with ID: 111");

                        insertNewScramble(newScramble);

                        if (mSuccessCreated){
                            Intent startScramblesActivity = new Intent(getActivity(), ScramblesActivity.class);
                            startActivity(startScramblesActivity);
                        }
                    }
            }
        }
    */
    protected String getRandom(String input) {
        char[] chars = input.toCharArray();
        boolean[] isUsed = new boolean[input.length()];
        for (int i = 0; i < chars.length; i++) isUsed[i] = false;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < chars.length; i++) {
            int r = random.nextInt(chars.length);
            while (isUsed[r]) {
                r = random.nextInt(chars.length);
            }
            isUsed[r] = true;
            char c = chars[r];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    public String calculate (String input) {

        String trans = "";

        boolean[] used = new boolean[input.length()];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c <= 'z' && c >= 'a') {
                used[i] = true;
            }
        }
        input = input.toUpperCase();

        String[] words = input.split("[.,!?:;]+|\\s+");
        String[] swapWords = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            swapWords[i] = getRandom(words[i]);
        }


        boolean isSentence = false;
        for (int i = 0, j = 0, k = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (c < 'A' || c > 'Z') {
                trans += input.charAt(i);
                if (isSentence) j++;
                k = 0;
                continue;
            }
            isSentence = true;
            char x;
            x = swapWords[j].charAt(k);
            k++;
            if (used[i])
                trans += Character.toLowerCase(x);
            else
                trans += x;

        }
        return trans;

    }

    public void insertNewScramble(Scramble scramble) {
        ContentValues scrambleValue = new ContentValues();

        scrambleValue.put(ScrambleEntry.COL_PHRASE, scramble.phrase);
        scrambleValue.put(ScrambleEntry.COL_CLUE, scramble.clue);
        scrambleValue.put(ScrambleEntry.COL_SCRAMBLED, scramble.answer);
        scrambleValue.put(ScrambleEntry.COL_AUTHOR, scramble.author);
        scrambleValue.put(ScrambleEntry.COL_IS_SOLVED, scramble.isSolved);
        scrambleValue.put(ScrambleEntry.COL_IDENTIFIER, scramble.identifier);
        scrambleValue.put(ScrambleEntry.COL_CORRECT, scramble.correct);

        try {
            getActivity().getContentResolver().insert(ScrambleEntry.CONTENT_URI, scrambleValue);

            mSuccessCreated = true;
        }catch (Exception e){
            e.getStackTrace();
        }
    }

}
