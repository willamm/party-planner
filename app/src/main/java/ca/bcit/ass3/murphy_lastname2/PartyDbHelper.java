package ca.bcit.ass3.murphy_lastname2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by willm_000 on 2017-10-29.
 */

public class PartyDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Party.db";

    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE " + PartyContract.Event.TABLE_NAME + " (" +
                    PartyContract.Event._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartyContract.Event.NAME + " TEXT, " +
                    PartyContract.Event.DATE + " NUMERIC, " +
                    PartyContract.Event.TIME + " NUMERIC);";

    private static final String SQL_CREATE_DETAILS_TABLE =
            "CREATE TABLE " + PartyContract.EventDetails.TABLE_NAME + " (" +
                    PartyContract.EventDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartyContract.EventDetails.ITEM_NAME + " TEXT, " +
                    PartyContract.EventDetails.ITEM_UNIT + " TEXT, " +
                    PartyContract.EventDetails.ITEM_QUANTITY + " INTEGER, " +
                    PartyContract.EventDetails.EVENT_ID + " INTEGER, " +
                    "FOREIGN KEY(" + PartyContract.EventDetails.EVENT_ID + ") " +
                    "REFERENCES " + PartyContract.Event.TABLE_NAME + "(" + PartyContract.Event._ID + ")" +
                    ");";

    private static final String SQL_CREATE_CONTRIBUTION_TABLE =
            "CREATE TABLE " + PartyContract.Contribution.TABLE_NAME + " (" +
                    PartyContract.Contribution._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartyContract.Contribution.NAME + " TEXT, " +
                    PartyContract.Contribution.QUANTITY + " INTEGER, " +
                    PartyContract.Contribution.DATE + " NUMERIC, " +
                    PartyContract.Contribution.DETAIL_ID + " INTEGER, " +
                    "FOREIGN KEY(" + PartyContract.Contribution.DETAIL_ID + ") " +
                    "REFERENCES " + PartyContract.EventDetails.TABLE_NAME + "(" + PartyContract.EventDetails._ID + ")" +
                    ");";

    public PartyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_DETAILS_TABLE);
        db.execSQL(SQL_CREATE_CONTRIBUTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
