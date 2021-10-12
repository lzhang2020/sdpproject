package edu.gatech.seclass.sdpscramble.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ScrambleContract {

    public static final String CONTENT_AUTHORITY = "edu.gatech.seclass.sdpscramble";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SCRAMBLES = "scrambles";
    public static final String PATH_PLAYERS = "players";
    public static final String PATH_STATISTICS = "statistics";

    public static final class ScrambleEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCRAMBLES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCRAMBLES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCRAMBLES;

        public static final String TABLE_NAME = "scrambles";

        public static final String _ID = "_id";
        public static final String COL_PHRASE = "phrase";
        public static final String COL_CLUE = "clue";
        public static final String COL_SCRAMBLED = "scrambled";
        public static final String COL_AUTHOR = "author";
        public static final String COL_IS_SOLVED = "is_solved";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_CORRECT = "correct";

        public static Uri buildScrambleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PlayerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYERS).build();

        public  static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYERS;

        public static final String TABLE_NAME = "players";

        public static final String _ID = "_id";
        public static final String COL_USERNAME = "username";
        public static final String COL_FIRSTNAME = "firstname";
        public static final String COL_LASTNAME = "lastname";
        public static final String COL_EMAIL = "email";
        public static final String COL_IS_LOGIN = "is_login";
        public static final String COL_CREATED_NUM = "created_num";
        public static final String COL_SOLVED_NUMBER = "solved_num";
        public static final String COL_SOLVED_BY_NUMBER = "solved_by_num";
        public static final String COL_PROCESS_SCRAMBLE = "processing_scramble";

        public static Uri buildPlayerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class StatisticEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STATISTICS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STATISTICS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STATISTICS;

        public static final String TABLE_NAME = "statistics";

        public static final String _ID = "_id";
        public static final String COL_PLAYER = "player";
        public static final String COL_SCRAMBLE_IDEN = "scramble_identifier";
        public static final String COL_STATUS = "status";
        public static final String COL_STAT_IDEN = "stat_identifier";

        public static Uri buildStatisticsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}