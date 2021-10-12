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

public class PlayerStatAdapter extends CursorAdapter{

    private static final String LOG_TAG = PlayerStatAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public PlayerStatAdapter (Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "StatAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    public static class ViewHolder {
        public final TextView mFirstNameTextView;
        public final TextView mLastNameTextView;
        public final TextView mSolvedTextView;
        public final TextView mCreatedTextView;
        public final TextView mSolvedByAveTextView;


        public ViewHolder(View view){
            mFirstNameTextView = (TextView) view.findViewById(R.id.tv_stat_player_fn);
            mLastNameTextView = (TextView) view.findViewById(R.id.tv_stat_player_ln);
            mSolvedTextView = (TextView) view.findViewById(R.id.tv_stat_player_solved);
            mCreatedTextView = (TextView) view.findViewById(R.id.tv_stat_player_created);
            mSolvedByAveTextView = (TextView) view.findViewById(R.id.tv_stat_player_ave_solved);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        int layoutId = R.layout.item_player_stat;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.mFirstNameTextView.setText(cursor.getString(ConstantUtils.COLUMN_FIRSTNAME));
        viewHolder.mLastNameTextView.setText(cursor.getString(ConstantUtils.COLUMN_LASTNAME));
        viewHolder.mSolvedTextView.setText(String.valueOf(cursor.getInt(ConstantUtils.COLUMN_SOLVED_NUM)));

        int createdNumber = cursor.getInt(ConstantUtils.COLUMN_CREATED_NUM);
        viewHolder.mCreatedTextView.setText(String.valueOf(createdNumber));

        int solvedByNumber = cursor.getInt(ConstantUtils.COLUMN_SOLVED_BY_NUM);
        double solvedByAve = 0;
        if (createdNumber != 0){
            solvedByAve = solvedByNumber/createdNumber;
        }
        viewHolder.mSolvedByAveTextView.setText(String.valueOf(solvedByAve));
    }
}
