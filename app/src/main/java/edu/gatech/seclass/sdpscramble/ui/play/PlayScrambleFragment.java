package edu.gatech.seclass.sdpscramble.ui.play;

import android.content.ContentValues;
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

import edu.gatech.seclass.sdpscramble.R;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;
import edu.gatech.seclass.sdpscramble.models.Player;
import edu.gatech.seclass.sdpscramble.models.Statistics;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;
import edu.gatech.seclass.sdpscramble.webservice.remote.ExternalWebService;
import edu.gatech.seclass.sdpscramble.webservice.remote.WebServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayScrambleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = PlayScrambleFragment.class.getSimpleName();

    private Cursor mDetailScrambleCursor = null;
    private Cursor mUserCursor = null;
    private Cursor mAuthorCursor = null;
    private int mPosition;
    private String mScrambled;
    private Uri mUri;
    private String mMessage;
    private int mCorrect = 0;
    private String mIdentifier = null;
    private String mUsername;
    private String mAuthor = null;
    private int mSolvedNum = -1;
    private int mSolvedByNum = -1;
    private int mStatus = 0;
    private String mProcessing;

    private TextView mScramblePhraseTextView;
    private TextView mScrambleClueTextView;
    private Button mSubmitScrambleButton;
    private EditText mAnswerView;

    private static final int SCRAMBLE_DETAIL_LOADER_ID = 201;
    private static final int PLAYER_DETAIL_LOADER_ID = 102;
    private static final int AUTHOR_DETAIL_LOADER_ID = 103;

    public PlayScrambleFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mUsername = getArguments().getString("current_user");
        mUri = Uri.parse(getArguments().getString("uri"));

        //Log.d(LOG_TAG, "current user: " + mUsername);
        getLoaderManager().initLoader(SCRAMBLE_DETAIL_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "entering oncreatview");

        View rootView = inflater.inflate(R.layout.fragment_scramble_play, container, false);

        mScramblePhraseTextView = (TextView) rootView.findViewById(R.id.tv_phrase_scramble_play);
        mScrambleClueTextView = (TextView) rootView.findViewById(R.id.tv_clue_scramble_play);
        mSubmitScrambleButton = (Button) rootView.findViewById(R.id.btn_scramble_submit);
        mAnswerView = (EditText) rootView.findViewById(R.id.et_input_answer);

        mSubmitScrambleButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String playerInput = mAnswerView.getText().toString();

                WebServiceInterface service = ExternalWebService.getInstance();

                if (playerInput.equals(mScrambled)){
                    mCorrect++;

                    //update local correct number of SCRAMBLE
                    ContentValues values = new ContentValues();
                    values.put(ScrambleEntry.COL_IS_SOLVED, 1);
                    values.put(ScrambleEntry.COL_CORRECT, mCorrect);

                    getActivity().getContentResolver().update(ScrambleEntry.CONTENT_URI,
                            values,
                            ScrambleEntry.COL_IDENTIFIER + "=?",
                            new String[]{mIdentifier});

                    //update online correct number of SCRAMBLE
                    Call<Integer> callCorrect = service.setCorrect(mIdentifier, mCorrect);
                    callCorrect.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d(LOG_TAG, "success");
                        }
                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(LOG_TAG, "failed");
                        }
                    });

                    //get the number of scrambles that player has solved
                    Cursor userCursor = getActivity().getContentResolver().query(PlayerEntry.CONTENT_URI,
                           ConstantUtils.PLAYER_COLUMNS,
                           PlayerEntry.COL_USERNAME + "=?",
                           new String[]{mUsername},
                           null);
                    userCursor.moveToFirst();

                    mSolvedNum = userCursor.getInt(ConstantUtils.COLUMN_SOLVED_NUM);
                    mSolvedNum++;

                    //update local solvedNumber of curren PLAYER
                    ContentValues userValues = new ContentValues();
                    userValues.put(PlayerEntry.COL_SOLVED_NUMBER, mSolvedNum);
                    getActivity().getContentResolver().update(PlayerEntry.CONTENT_URI,
                            userValues,
                            PlayerEntry.COL_USERNAME + "=?",
                            new String[]{mUsername});

                    //update online solvedNumber of current PLAYER
                    Call<Integer> callSolved = service.setSolvedNumber(mUsername, mSolvedNum);
                    callSolved.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d(LOG_TAG, "success");
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(LOG_TAG, "failed");
                        }
                    });

                    //get the number of scramble that solved by others
                    Cursor authorCursor = getActivity().getContentResolver().query(PlayerEntry.CONTENT_URI,
                            ConstantUtils.PLAYER_COLUMNS,
                            PlayerEntry.COL_USERNAME + "=?",
                            new String[]{mAuthor},
                            null);
                    authorCursor.moveToFirst();

                    mSolvedByNum = authorCursor.getInt(ConstantUtils.COLUMN_SOLVED_BY_NUM);
                    mSolvedByNum++;

                    //update local solvedByNumber of the scramble author
                    ContentValues authorValues = new ContentValues();
                    authorValues.put(PlayerEntry.COL_SOLVED_BY_NUMBER, mSolvedByNum);
                    getActivity().getContentResolver().update(PlayerEntry.CONTENT_URI,
                            authorValues,
                            PlayerEntry.COL_USERNAME + "=?",
                            new String[] {mAuthor});

                    //update online solvedByNumber of the scramble author
                    Call<Integer> callSolvedBy = service.setSolvedByNumber(mAuthor, mSolvedByNum);
                    callSolvedBy.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d(LOG_TAG, "success");
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(LOG_TAG, "failed");
                        }
                    });

                    mStatus = 1;
                    mProcessing = "";

                    mMessage = "Congratuations! You are right!";
                } else {
                    mStatus = 2;
                    mProcessing = mIdentifier;

                    mMessage = "Sorry, please try again.";
                }

                //update local processing scramble of current PLAYER
                ContentValues userProcessingValues = new ContentValues();
                userProcessingValues.put(PlayerEntry.COL_PROCESS_SCRAMBLE, mProcessing);
                getActivity().getContentResolver().update(PlayerEntry.CONTENT_URI,
                        userProcessingValues,
                        PlayerEntry.COL_USERNAME + "=?",
                        new String[]{mUsername});

                //update online processing scramble of current PLAYER
                Call<String> callProcessing = service.setProcessing(mUsername, mProcessing);
                callProcessing.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(LOG_TAG, "success");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(LOG_TAG, "failed");
                    }
                });

                String combineIdentifier = mUsername + "_" + mIdentifier;

                ContentValues statValues = new ContentValues();
                statValues.put(StatisticEntry.COL_PLAYER, mUsername);
                statValues.put(StatisticEntry.COL_SCRAMBLE_IDEN, mIdentifier);
                statValues.put(StatisticEntry.COL_STAT_IDEN, combineIdentifier);
                statValues.put(StatisticEntry.COL_STATUS, mStatus);

                //set local (player, scramble) status
                getActivity().getContentResolver().insert(StatisticEntry.CONTENT_URI, statValues);

                //set online (player, scramble) status
                Statistics newStats = new Statistics(mUsername, mIdentifier, mStatus);
                Call<Statistics> callStats = service.createStats(newStats.getStatIdentifier(), newStats);
                callStats.enqueue(new Callback<Statistics>() {
                    @Override
                    public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                        Log.d(LOG_TAG, "success");
                    }

                    @Override
                    public void onFailure(Call<Statistics> call, Throwable t) {
                        Log.d(LOG_TAG, "failed");
                    }
                });

                Toast.makeText(getActivity(), mMessage, Toast.LENGTH_SHORT).show();

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
        Log.d(LOG_TAG, "entering oncreateloader");
        switch (loaderId) {
            case SCRAMBLE_DETAIL_LOADER_ID: {
                Log.d(LOG_TAG, "mUri: " + mUri);
                return new CursorLoader(getActivity(),
                        mUri,
                        ConstantUtils.SCRAMBLE_COLUMNS,
                        null,
                        null,
                        null);
            }
            default: {
                throw new RuntimeException("Loader not implemented: " + loaderId);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mDetailScrambleCursor = data;

        Log.d(LOG_TAG, "number of detail scramble cursor data: " + data.getCount());
        mDetailScrambleCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);
        mScramblePhraseTextView.setText(mDetailScrambleCursor.getString(ConstantUtils.COLUMN_PHRASE));
        mScrambleClueTextView.setText(mDetailScrambleCursor.getString(ConstantUtils.COLUMN_CLUE));

        mScrambled = mDetailScrambleCursor.getString(ConstantUtils.COLUMN_SCRAMBLED);

        mCorrect = mDetailScrambleCursor.getInt(ConstantUtils.COLUMN_CORRECT);
        mIdentifier = mDetailScrambleCursor.getString(ConstantUtils.COLUMN_SCRAMBLE_IDENTIFIER);
        mAuthor = mDetailScrambleCursor.getString(ConstantUtils.COLUMN_AUTHOR);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "entering onloaderreset");
        mDetailScrambleCursor = null;
        mCorrect = 0;
        mIdentifier = null;
    }
}
