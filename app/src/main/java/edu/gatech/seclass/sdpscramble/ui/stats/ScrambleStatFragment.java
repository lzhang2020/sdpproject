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
import edu.gatech.seclass.sdpscramble.adapters.ScrambleStatAdapter;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;


public class ScrambleStatFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = ScrambleStatFragment.class.getSimpleName();

    public static final String DEFAULT_PLAYER_URI = "PLAYER_URI";
    public static final String DEFAULT_SCRAMBLE_URI = "SCRAMBLE_URI";
    public static final String DEFAULT_STATS_URI = "STATS_URI";

    private static final int PLAYER_LOADER_ID = 10;
    private static final int SCRAMBLE_LOADER_ID = 20;
    private static final int STATS_LOADER_ID = 30;
    private static final int PLAYER_STATS_LOADER_ID = 301;
    private static int CURSOR_LOADER_ID;

    private String mUsername;
    private Cursor mUserCursor;
    private ScrambleStatAdapter mScrambleStatAdapter;
    private ListView mScrambleStatListView;

    private String mLoader;
    private boolean mUserLoaded = false;

    public ScrambleStatFragment () {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_scramble_stat, container, false);
        mScrambleStatAdapter = new ScrambleStatAdapter(getActivity(), null, 0, SCRAMBLE_LOADER_ID);

        mScrambleStatListView = (ListView) rootView.findViewById(R.id.lv_scrambble_stat);

        mScrambleStatListView.setAdapter(mScrambleStatAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(SCRAMBLE_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId){
            case SCRAMBLE_LOADER_ID: {
                String sortOrder = ScrambleEntry.COL_CORRECT + " DESC";
                return new CursorLoader(getActivity(),
                        ScrambleEntry.CONTENT_URI,
                        null,
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mScrambleStatAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mScrambleStatAdapter = null;
    }
}
