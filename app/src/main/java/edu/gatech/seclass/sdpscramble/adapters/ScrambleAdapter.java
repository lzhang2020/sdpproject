package edu.gatech.seclass.sdpscramble.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;

import static android.content.ContentValues.TAG;

public class ScrambleAdapter extends RecyclerView.Adapter<ScrambleAdapter.ScrambleViewHolder>{
    private static final String LOG_TAG = ScrambleAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    private ScrambleAdapterOnClickHandler mScrambleOnClickHandler;

    private View mEmptyView;

    private static int viewHolderCount;


    public static interface ScrambleAdapterOnClickHandler{
        void onClick(long l, ScrambleViewHolder scrambleViewHolder);
    }

    public ScrambleAdapter(@NonNull Context context, ScrambleAdapterOnClickHandler movieAdapterOnClickHandler) {
        //public MovieAdapter(@NonNull Context context) {
        mContext = context;
        mScrambleOnClickHandler = movieAdapterOnClickHandler;
    }

    @Override
    public ScrambleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        if (viewGroup instanceof RecyclerView) {
            int layoutIdForListItem = R.layout.item_scramble;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            view.setFocusable(true);

            ScrambleViewHolder viewHolder = new ScrambleViewHolder(view);

            return viewHolder;
        }else {
            throw new RuntimeException("Not bound to Recyclerview.");
        }
    }

    @Override
    public void onBindViewHolder(ScrambleViewHolder scrambleViewHolder, int position) {
        Log.d(LOG_TAG, "#" + position);

        if (! mCursor.moveToFirst()){
            return;
        }

        mCursor.moveToPosition(position);

        final String scramblePhrase = mCursor.getString(ConstantUtils.COLUMN_PHRASE);
        scrambleViewHolder.mPhraseTextView.setText(scramblePhrase);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor){
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public class ScrambleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mPhraseTextView;

        public ScrambleViewHolder(View itemView) {
            super(itemView);

            mPhraseTextView = (TextView) itemView.findViewById(R.id.tv_scramble_phrase);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);

            int columnMovieIdIndex = mCursor.getColumnIndex(ScrambleEntry._ID);

            long scrambleId = mCursor.getLong(columnMovieIdIndex);

            mScrambleOnClickHandler.onClick(scrambleId, this);
        }

    }
}
/*
public class ScrambleAdapter extends CursorAdapter {


    private Context mContext;
    private static int sLoaderID;

    public ScrambleAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "ScrambleAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    public static class ViewHolder {
        public final TextView mPhraseTextView;
        //public final TextView mClueTextView;

        public ViewHolder(View view){
            mPhraseTextView = (TextView) view.findViewById(R.id.tv_scramble_phrase);
            //mClueTextView = (TextView) view.findViewById(R.id.scramble_clue);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        int layoutId = R.layout.item_scramble;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");

        //int phraseIndex = cursor.getColumnIndex(ScrambleEntry.COL_PHRASE);
        final String scramblePhrase = cursor.getString(ConstantUtils.COLUMN_PHRASE);
        viewHolder.mPhraseTextView.setText(scramblePhrase);

        //int clueIndex = cursor.getColumnIndex(ScrambleEntry.COL_CLUE);
        //final String scrambleClue = cursor.getString(clueIndex);

        //viewHolder.mClueTextView.setText(scrambleClue);
    }
}
*/