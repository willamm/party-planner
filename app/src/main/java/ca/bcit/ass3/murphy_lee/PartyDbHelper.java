package ca.bcit.ass3.murphy_lee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PartyDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

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
                    "ON DELETE CASCADE" +
                    ");";

    private static PartyDbHelper instance;

    public static synchronized PartyDbHelper getInstance(Context context) {
        if (instance == null) {
            return new PartyDbHelper(context);
        }
        return instance;
    }

    private PartyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PartyContract.Contribution.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PartyContract.EventDetails.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PartyContract.EventMaster.TABLE_NAME);
        onCreate(db);
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
