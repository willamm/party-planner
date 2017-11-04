package ca.bcit.ass3.murphy_lastname2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PartyDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "Party.db";

    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE " + PartyContract.EventMaster.TABLE_NAME + " (" +
                    PartyContract.EventMaster._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartyContract.EventMaster.NAME + " TEXT, " +
                    PartyContract.EventMaster.DATE + " NUMERIC, " +
                    PartyContract.EventMaster.TIME + " NUMERIC);";

    private static final String SQL_CREATE_DETAILS_TABLE =
            "CREATE TABLE " + PartyContract.EventDetails.TABLE_NAME + " (" +
                    PartyContract.EventDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartyContract.EventDetails.ITEM_NAME + " TEXT, " +
                    PartyContract.EventDetails.ITEM_UNIT + " TEXT, " +
                    PartyContract.EventDetails.ITEM_QUANTITY + " INTEGER, " +
                    PartyContract.EventDetails.EVENT_ID + " INTEGER, " +
                    "FOREIGN KEY(" + PartyContract.EventDetails.EVENT_ID + ") " +
                    "REFERENCES " + PartyContract.EventMaster.TABLE_NAME + "(" + PartyContract.EventMaster._ID + ")" +
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

    private static final String SQL_INITIAL_DATA =
            "INSERT INTO " + PartyContract.EventMaster.TABLE_NAME +
                    "(" + PartyContract.EventMaster.NAME + ", " + PartyContract.EventMaster.DATE + ", " + PartyContract.EventMaster.TIME + ")" +
                    "VALUES('Christmas Party', 'December 20, 2017', '12:30 PM');" +
                    "INSERT INTO " + PartyContract.EventDetails.TABLE_NAME +
                    "VALUES('Paper plates', 'Pieces', 20, 1);";

    PartyDbHelper(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + PartyContract.EventMaster.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PartyContract.EventDetails.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PartyContract.Contribution.TABLE_NAME);
        onCreate(db);
        db.execSQL(SQL_INITIAL_DATA);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
