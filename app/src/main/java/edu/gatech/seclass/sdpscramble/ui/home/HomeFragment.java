package edu.gatech.seclass.sdpscramble.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import java.util.ArrayList;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.ui.register.RegistrationActivity;
import edu.gatech.seclass.sdpscramble.ui.scrambles.ScramblesActivity;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;
import edu.gatech.seclass.sdpscramble.utils.HelperUtils;

import static edu.gatech.seclass.sdpscramble.utils.HelperUtils.isInList;


public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = HomeFragment.class.getName();

    public static final String DEFAULT_PLAYER_URI = "PLAYER_URI";
    public static final String DEFAULT_SCRAMBLE_URI = "SCRAMBLE_URI";
    public static final String DEFAULT_STATS_URI = "STATS_URI";

    private static final int PLAYER_LOADER_ID = 10;
    private static final int SCRAMBLE_LOADER_ID = 20;
    private static final int STATS_LOADER_ID = 30;

    private static final int LOGIN_PLAYER_LOADER_ID = 101;


    //private Cursor mCurrentUserCursor;
    private Uri mUri;

    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mInputUser;

    private String mCurrentUser;

    private ArrayList<String> mUserArrayList = new ArrayList<String>();
    private int mLogin = 0;

    public HomeFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(LOG_TAG, "Home fregmane started");

        getLoaderManager().initLoader(LOGIN_PLAYER_LOADER_ID, null, this);

        if (mLogin > 0){
            Intent startScramblesActivity = new Intent(getActivity(), ScramblesActivity.class);
            startActivity(startScramblesActivity);
        } else {
            getLoaderManager().initLoader(PLAYER_LOADER_ID, null, this);
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();

        if (arguments != null) {
            mUri = arguments.getParcelable(DEFAULT_PLAYER_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mLoginButton = (Button) rootView.findViewById(R.id.btn_login);
        mSignUpButton = (Button) rootView.findViewById(R.id.btn_singup);

        mInputUser = (EditText) rootView.findViewById(R.id.et_input_username);

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String playerInput = mInputUser.getText().toString();

                if (HelperUtils.isInList(playerInput, mUserArrayList)) {
                    ContentValues values = new ContentValues();
                    values.put(PlayerEntry.COL_IS_LOGIN, 1);

                    getActivity().getContentResolver().update(PlayerEntry.CONTENT_URI,
                            values,
                            PlayerEntry.COL_USERNAME + "=?",
                            new String[]{playerInput});

                    Intent startScramblesActivity = new Intent(getActivity(), ScramblesActivity.class);
                    startScramblesActivity.putExtra("current_user", playerInput);
                    startActivity(startScramblesActivity);
                } else {
                    Toast.makeText(getActivity(), "User does not exist!", Toast.LENGTH_SHORT).show();

                    Intent startRegistrationActivity = new Intent(getActivity(), RegistrationActivity.class);
                    startActivity(startRegistrationActivity);
                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(HomeActivity.this, "RegistrationActivity has not been implement yet", Toast.LENGTH_SHORT).show();
                //COMPLETED: add intent redirecting to registration page
                Intent startRegistrationActivity = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(startRegistrationActivity);
            }
        });

        return rootView;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId){
            case PLAYER_LOADER_ID:{
                return new CursorLoader(
                        getActivity(),
                        PlayerEntry.CONTENT_URI,
                        ConstantUtils.PLAYER_COLUMNS,
                        null,
                        null,
                        null
                );
            }

            case LOGIN_PLAYER_LOADER_ID:{
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLogin = cursor.getCount();

        Log.d(LOG_TAG, "number: " + mLogin);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            mUserArrayList.add(cursor.getString(ConstantUtils.COLUMN_USERNAME));
        }

        Log.d(LOG_TAG, "Number of mUser: " + mUserArrayList.size());
        for (String s: mUserArrayList){
            Log.d(LOG_TAG, "user: " + s);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUserArrayList = new ArrayList<String>();
    }

}