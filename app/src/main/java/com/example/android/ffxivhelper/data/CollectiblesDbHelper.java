package com.example.android.ffxivhelper.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.ffxivhelper.data.CollectiblesContract.MountEntry;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CollectiblesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = CollectiblesDbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "store.db";

    private static String DATABASE_PATH = "/data/data/com.example.android.ffxivhelper/databases/";

    private SQLiteDatabase mDatabase;

    private final Context mContext;

    public CollectiblesDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if(dbExist) {
            //do nothing - database already exist
        }
        else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try{
            String dbPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e) {
            //database does't exist yet.
        }
        if(checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if(mDatabase != null)
            mDatabase.close();
        super.close();
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        String SQL_CREATE_STORE_TABLE = "CREATE TABLE " +
                MountEntry.TABLE_NAME + "(" +
                MountEntry._ID + " INTEGER PRIMARY KEY, " +
                MountEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MountEntry.COLUMN_IMAGE + " TEXT, " +
                MountEntry.COLUMN_TAGS + " TEXT, " +
                MountEntry.COLUMN_CHAR1 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR2 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR3 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR4 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR5 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR6 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR7 + " BIT NOT NULL, " +
                MountEntry.COLUMN_CHAR8 + " BIT NOT NULL);";
        Log.d(LOG_TAG,SQL_CREATE_STORE_TABLE);
        db.execSQL(SQL_CREATE_STORE_TABLE);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}