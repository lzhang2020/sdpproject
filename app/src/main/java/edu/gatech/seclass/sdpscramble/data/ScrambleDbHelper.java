package edu.gatech.seclass.sdpscramble.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;

public class ScrambleDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;

    static final String DATABASE_NAME = "sdpscrambles.db";

    ScrambleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PLAYERS_TABLE = "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PlayerEntry.COL_USERNAME + " TEXT NOT NULL, " +
                PlayerEntry.COL_FIRSTNAME + " TEXT, " +
                PlayerEntry.COL_LASTNAME + " TEXT, " +
                PlayerEntry.COL_EMAIL + " TEXT, " +
                PlayerEntry.COL_IS_LOGIN + " INTEGER NOT NULL, " +
                PlayerEntry.COL_CREATED_NUM + " INTEGER NOT NULL, " +
                PlayerEntry.COL_SOLVED_NUMBER + " INTEGER NOT NULL, " +
                PlayerEntry.COL_SOLVED_BY_NUMBER + " INTEGER NOT NULL, " +
                PlayerEntry.COL_PROCESS_SCRAMBLE + " TEXT NOT NULL, " +
                " UNIQUE (" + PlayerEntry.COL_USERNAME + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_SCRAMBLES_TABLE = "CREATE TABLE " + ScrambleEntry.TABLE_NAME + " (" +
                ScrambleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScrambleEntry.COL_PHRASE + " TEXT NOT NULL, " +
                ScrambleEntry.COL_SCRAMBLED + " TEXT NOT NULL, " +
                ScrambleEntry.COL_CLUE + " TEXT NOT NULL, " +
                ScrambleEntry.COL_AUTHOR + " TEXT NOT NULL, " +
                ScrambleEntry.COL_IS_SOLVED + " INTEGER NOT NULL, " +
                ScrambleEntry.COL_IDENTIFIER+ " TEXT NOT NULL, " +
                ScrambleEntry.COL_CORRECT + " INTEGER NOT NULL, " +
                "UNIQUE (" + ScrambleEntry.COL_IDENTIFIER + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_STAT_TABLE = "CREATE TABLE " + StatisticEntry.TABLE_NAME + " (" +
                StatisticEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StatisticEntry.COL_PLAYER + " TEXT NOT NULL, " +
                StatisticEntry.COL_SCRAMBLE_IDEN + " TEXT NOT NULL, " +
                StatisticEntry.COL_STATUS + " INTEGER NOT NULL, " +
                StatisticEntry.COL_STAT_IDEN + " TEXT NOT NULL, " +
                "UNIQUE (" + StatisticEntry.COL_STAT_IDEN + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_PLAYERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SCRAMBLES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlayerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScrambleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StatisticEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}
