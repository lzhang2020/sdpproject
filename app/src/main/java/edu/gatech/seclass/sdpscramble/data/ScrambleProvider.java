package edu.gatech.seclass.sdpscramble.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import edu.gatech.seclass.sdpscramble.data.ScrambleContract.PlayerEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.ScrambleEntry;
import edu.gatech.seclass.sdpscramble.data.ScrambleContract.StatisticEntry;

public class ScrambleProvider extends ContentProvider {
    private static final String LOG_TAG = ScrambleProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ScrambleDbHelper mOpenHelper;

    private static final int PLAYER = 100;
    private static final int PLAYER_IDX = 101;

    private static final int SCRAMBLE = 200;
    private static final int SCRAMBLE_IDX = 201;

    private static final int STAT = 300;
    private static final int STAT_IDX = 301;

    private static final String mScrambleIdSelection =
            ScrambleEntry.TABLE_NAME + "." + ScrambleEntry._ID + "=?";
    private static final String mPlayerIdSelection =
            PlayerEntry.TABLE_NAME + "." + PlayerEntry._ID + "=?";

    private static final String mStatIdSelection =
            ScrambleEntry.TABLE_NAME + "." + StatisticEntry._ID + "=?";

    private static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ScrambleContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ScrambleEntry.TABLE_NAME, SCRAMBLE);
        matcher.addURI(authority, ScrambleEntry.TABLE_NAME + "/#", SCRAMBLE_IDX);

        matcher.addURI(authority, PlayerEntry.TABLE_NAME, PLAYER);
        matcher.addURI(authority, PlayerEntry.TABLE_NAME + "/#", PLAYER_IDX);

        matcher.addURI(authority, StatisticEntry.TABLE_NAME, STAT);
        matcher.addURI(authority, StatisticEntry.TABLE_NAME + "/#", STAT_IDX);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ScrambleDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case PLAYER:{
                return PlayerEntry.CONTENT_TYPE;
            }
            case PLAYER_IDX:{
                return PlayerEntry.CONTENT_ITEM_TYPE;
            }
            case SCRAMBLE:{
                return ScrambleEntry.CONTENT_TYPE;
            }
            case SCRAMBLE_IDX:{
                return ScrambleEntry.CONTENT_ITEM_TYPE;
            }
            case STAT:{
                return StatisticEntry.CONTENT_TYPE;
            }
            case STAT_IDX:{
                return StatisticEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){

            case PLAYER:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PlayerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case PLAYER_IDX:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PlayerEntry.TABLE_NAME,
                        projection,
                        PlayerEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            case SCRAMBLE:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ScrambleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case SCRAMBLE_IDX:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ScrambleEntry.TABLE_NAME,
                        projection,
                        ScrambleEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            case STAT:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        StatisticEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case STAT_IDX:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        StatisticEntry.TABLE_NAME,
                        projection,
                        StatisticEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case PLAYER: {
                long _id = db.insert(PlayerEntry.TABLE_NAME, null, contentValues);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = PlayerEntry.buildPlayerUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case SCRAMBLE: {
                long _id = db.insert(ScrambleEntry.TABLE_NAME, null, contentValues);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = ScrambleEntry.buildScrambleUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case STAT: {
                long _id = db.insert(StatisticEntry.TABLE_NAME, null, contentValues);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = StatisticEntry.buildStatisticsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match){
            case PLAYER: {

                db.beginTransaction();

                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;

                        _id = db.insertOrThrow(PlayerEntry.TABLE_NAME,
                                null, value);

                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            }
            case SCRAMBLE: {
                db.beginTransaction();

                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;

                        _id = db.insertOrThrow(ScrambleEntry.TABLE_NAME, null, value);

                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {

                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (numInserted > 0) {

                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            }
            case STAT: {
                db.beginTransaction();

                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;

                        _id = db.insertOrThrow(StatisticEntry.TABLE_NAME, null, value);

                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {

                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (numInserted > 0) {

                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            }
            default: {
                return super.bulkInsert(uri, values);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case PLAYER: {
                numDeleted = db.delete(
                        PlayerEntry.TABLE_NAME, selection, selectionArgs);

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        PlayerEntry.TABLE_NAME + "'");
                break;
            }
            case PLAYER_IDX: {
                numDeleted = db.delete(PlayerEntry.TABLE_NAME,
                        PlayerEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        PlayerEntry.TABLE_NAME + "'");

                break;
            }
            case SCRAMBLE: {
                numDeleted = db.delete(
                        ScrambleEntry.TABLE_NAME, selection, selectionArgs);

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        ScrambleEntry.TABLE_NAME + "'");
                break;
            }
            case SCRAMBLE_IDX: {
                numDeleted = db.delete(ScrambleEntry.TABLE_NAME,
                        ScrambleEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        ScrambleEntry.TABLE_NAME + "'");

                break;
            }
            case STAT: {
                numDeleted = db.delete(
                        StatisticEntry.TABLE_NAME, selection, selectionArgs);

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        StatisticEntry.TABLE_NAME + "'");
                break;
            }
            case STAT_IDX: {
                numDeleted = db.delete(StatisticEntry.TABLE_NAME,
                        StatisticEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        StatisticEntry.TABLE_NAME + "'");

                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case PLAYER:{
                numUpdated = db.update(PlayerEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case PLAYER_IDX: {
                numUpdated = db.update(PlayerEntry.TABLE_NAME,
                        contentValues,
                        PlayerEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case SCRAMBLE:{
                numUpdated = db.update(ScrambleEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case SCRAMBLE_IDX: {
                numUpdated = db.update(ScrambleEntry.TABLE_NAME,
                        contentValues,
                        ScrambleEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case STAT:{
                numUpdated = db.update(StatisticEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case STAT_IDX: {
                numUpdated = db.update(StatisticEntry.TABLE_NAME,
                        contentValues,
                        StatisticEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
