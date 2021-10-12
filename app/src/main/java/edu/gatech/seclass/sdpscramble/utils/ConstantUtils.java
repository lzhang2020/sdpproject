package edu.gatech.seclass.sdpscramble.utils;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;

public class ConstantUtils {

    public static final String[] PLAYER_COLUMNS = {
            PlayerEntry.TABLE_NAME + "." + PlayerEntry._ID,
            PlayerEntry.COL_USERNAME,
            PlayerEntry.COL_FIRSTNAME,
            PlayerEntry.COL_LASTNAME,
            PlayerEntry.COL_EMAIL,
            PlayerEntry.COL_IS_LOGIN,
            PlayerEntry.COL_CREATED_NUM,
            PlayerEntry.COL_SOLVED_NUMBER,
            PlayerEntry.COL_SOLVED_BY_NUMBER,
            PlayerEntry.COL_PROCESS_SCRAMBLE
    };

    public static final String[] STAT_COLUMNS = {
            StatisticEntry.TABLE_NAME + "." + StatisticEntry._ID,
            StatisticEntry.COL_PLAYER,
            StatisticEntry.COL_SCRAMBLE_IDEN,
            StatisticEntry.COL_STATUS,
            StatisticEntry.COL_STAT_IDEN
    };

    public static final String[] SCRAMBLE_COLUMNS = {
            ScrambleEntry.TABLE_NAME + "." + ScrambleEntry._ID,
            ScrambleEntry.COL_PHRASE,
            ScrambleEntry.COL_SCRAMBLED,
            ScrambleEntry.COL_CLUE,
            ScrambleEntry.COL_AUTHOR,
            ScrambleEntry.COL_IS_SOLVED,
            ScrambleEntry.COL_IDENTIFIER,
            ScrambleEntry.COL_CORRECT,
    };

    public static final int COLUMN_PLAYER_ID = 0;
    public static final int COLUMN_USERNAME = 1;
    public static final int COLUMN_FIRSTNAME = 2;
    public static final int COLUMN_LASTNAME = 3;
    public static final int COLUMN_EMAIL = 4;
    public static final int COLUMN_IS_LOGIN = 5;
    public static final int COLUMN_CREATED_NUM = 6;
    public static final int COLUMN_SOLVED_NUM = 7;
    public static final int COLUMN_SOLVED_BY_NUM = 8;
    public static final int COLUMN_PROCESSING_SCRAMBLE = 9;

    public static final int COLUMN_STAT_ID = 0;
    public static final int COLUMN_STAT_PLAYER = 1;
    public static final int COLUMN_SCRAMBLE_IDEN = 2;
    public static final int COLUMN_STATUS = 3;
    public static final int COLUMN_STAT_IDEN = 4;

    public static final int COLUMN_SCRAMBLE_ID = 0;
    public static final int COLUMN_PHRASE = 1;
    public static final int COLUMN_SCRAMBLED = 2;
    public static final int COLUMN_CLUE = 3;
    public static final int COLUMN_AUTHOR = 4;
    public static final int COLUMN_IS_SOLVED = 5;
    public static final int COLUMN_SCRAMBLE_IDENTIFIER = 6;
    public static final int COLUMN_CORRECT = 7;
}