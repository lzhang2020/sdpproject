package edu.gatech.seclass.sdpscramble.ui.scrambles;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Vector;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.adapters.ScrambleAdapter;
import edu.gatech.seclass.sdpscramble.ui.play.PlayScrambleActivity;
import edu.gatech.seclass.sdpscramble.ui.play.PlayScrambleFragment;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;

public class ScramblesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = ScramblesFragment.class.getSimpleName();

    private ScrambleAdapter mScrambleAdapter;
    private ListView mScrambleListView;
    private String mUsername;
    private RecyclerView mScrambleRecyclerView;

    private int mPosition = RecyclerView.NO_POSITION;

    private static final int CURSOR_LOADER_ID = 0;

    public ScramblesFragment(){}

    public interface Callback {
        public void onItemSelected(Uri scrambleUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        /*
        Cursor c =
                getActivity().getContentResolver().query(ScrambleEntry.CONTENT_URI,
                        new String[]{ScrambleEntry._ID},
                        null,
                        null,
                        null);
        if (c.getCount() == 0){
            //insertScrambleData();
        }*/
        // initialize loader
        mUsername = getArguments().getString("current_user");
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final View rootView = inflater.inflate(R.layout.fragment_scrambles, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_scrambles, container, false);

        mScrambleRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_scrambles);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //mScrambleRecyclerView.setHasFixedSize(true);
        mScrambleRecyclerView.setLayoutManager(layoutManager);

        mScrambleAdapter = new ScrambleAdapter(getActivity(), new ScrambleAdapter.ScrambleAdapterOnClickHandler(){
            @Override
            public void onClick(long scrambleId, ScrambleAdapter.ScrambleViewHolder scrambleViewHolder){
                mPosition = scrambleViewHolder.getAdapterPosition();

                Uri scrambleUri = ScrambleEntry.buildScrambleUri(scrambleId);

                ((Callback) getActivity()).onItemSelected(scrambleUri);
            }
        });

        mScrambleRecyclerView.setAdapter(mScrambleAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                ScrambleEntry.CONTENT_URI,
                ConstantUtils.SCRAMBLE_COLUMNS,
                null,
                null,
                null);
    }

    /*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
    */

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(cursor.getCount());

            do {
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                contentValuesVector.add(contentValues);
            } while (cursor.moveToNext());

            mScrambleAdapter.swapCursor(cursor);
        }

        Log.d(LOG_TAG, "number of scrambles loaded: " + cursor.getCount());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mScrambleAdapter.swapCursor(null);
    }
}