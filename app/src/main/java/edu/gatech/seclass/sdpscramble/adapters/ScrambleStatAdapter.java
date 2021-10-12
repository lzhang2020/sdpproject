package edu.gatech.seclass.sdpscramble.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.utils.ConstantUtils;

public class ScrambleStatAdapter extends CursorAdapter {
    private static final String LOG_TAG = ScrambleStatAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public ScrambleStatAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "ScrambleStatAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    public static class ViewHolder {
        public final TextView mIdentifierTextView;
        public final TextView mIsSolvedTextView;
        public final TextView mIsCreatedTextView;
        public final TextView mTotalSolvedTextView;

        public ViewHolder(View view){
            mIdentifierTextView = (TextView) view.findViewById(R.id.tv_stat_scramble_identifier);
            mIsSolvedTextView = (TextView) view.findViewById(R.id.tv_stat_scramble_is_solved_by_user);
            mIsCreatedTextView = (TextView) view.findViewById(R.id.tv_stat_scramble_is_Created_by_user);
            mTotalSolvedTextView = (TextView) view.findViewById(R.id.tv_stat_scramble_solved_total);

        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        int layoutId = R.layout.item_scramble_stat;

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
        //final String scramblePhrase = cursor.getString(ConstantUtils.COLUMN_PHRASE);
        viewHolder.mIdentifierTextView.setText(cursor.getString(ConstantUtils.COLUMN_SCRAMBLE_IDENTIFIER));

        int isSolvedByUser = cursor.getInt(ConstantUtils.COLUMN_IS_SOLVED);

        /*
        if (isSolvedByUser == 1){
            viewHolder.mIsSolvedTextView.setText("Yes");
        } else {
            viewHolder.mIsSolvedTextView.setText("No");
        }*/

        viewHolder.mIsSolvedTextView.setText(isSolvedByUser == 1 ? "Yes" : isSolvedByUser == 0 ? "No": "IP");

        viewHolder.mIsCreatedTextView.setText(String.valueOf(cursor.getString(ConstantUtils.COLUMN_AUTHOR)));

        viewHolder.mTotalSolvedTextView.setText(String.valueOf(cursor.getInt(ConstantUtils.COLUMN_CORRECT)));

        //int clueIndex = cursor.getColumnIndex(ScrambleEntry.COL_CLUE);
        //final String scrambleClue = cursor.getString(clueIndex);

        //viewHolder.mClueTextView.setText(scrambleClue);
    }
}
