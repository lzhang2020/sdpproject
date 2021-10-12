package edu.gatech.seclass.sdpscramble.ui.stats;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.adapters.PlayerStatAdapter;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;

public class PlayerStatFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DEFAULT_PLAYER_URI = "PLAYER_URI";
    public static final String DEFAULT_SCRAMBLE_URI = "SCRAMBLE_URI";
    public static final String DEFAULT_STATS_URI = "STATS_URI";

    private static final int PLAYER_LOADER_ID = 10;
    private static final int SCRAMBLE_LOADER_ID = 20;
    private static final int STATS_LOADER_ID = 30;
    private static final int PLAYER_STATS_LOADER_ID = 301;
    private static int CURSOR_LOADER_ID;

    private String mUsername;
    private String mFirstname;
    private String mLastname;
    private Cursor mUserCursor;
    private PlayerStatAdapter mPlayerStatAdapter;
    private ListView mPlayerStatListView;

    private int mUserSolvedNumber;
    private int mUserCreatedNumber;
    private int mOthersSolvedNumber;
    private int mSolvedAverage;

    private String mLoader;


    private boolean mUserLoaded = false;
    //private Button mAllStatButton;
    //private Button mPlayerStatButton;

    public PlayerStatFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        mUsername = getArguments().getString("current_user");

        getLoaderManager().initLoader(PLAYER_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_player_stat, container, false);

        mPlayerStatAdapter = new PlayerStatAdapter(getActivity(), null, 0, PLAYER_LOADER_ID);

        mPlayerStatListView = (ListView) rootView.findViewById(R.id.lv_player_stat);

        mPlayerStatListView.setAdapter(mPlayerStatAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId){
            case PLAYER_LOADER_ID: {
                String sortOrder = PlayerEntry.COL_SOLVED_NUMBER + " DESC";
                return new CursorLoader(getActivity(),
                        PlayerEntry.CONTENT_URI,
                        ConstantUtils.PLAYER_COLUMNS,
                        null,
                        null,
                        sortOrder);
            }
            default:{
                throw new RuntimeException("Loader not implemented: " + loaderId);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mPlayerStatAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlayerStatAdapter = null;
    }
}
