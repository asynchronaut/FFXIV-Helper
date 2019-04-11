package com.example.android.ffxivhelper;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.ffxivhelper.data.CollectiblesContract.MountEntry;
import com.example.android.ffxivhelper.data.CollectiblesDbHelper;

public class DatabaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mDatabaseListView;
    private CollectibleCursorAdapter mCursorAdapter;
    private CollectiblesDbHelper dbHelper;

    private final int LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        mDatabaseListView = findViewById(R.id.lv_database);

        mCursorAdapter = new CollectibleCursorAdapter(this, null);
        mDatabaseListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(LOADER_ID,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MountEntry._ID,
                MountEntry.COLUMN_NAME,
                MountEntry.COLUMN_CHAR1};
        String selection = "char1 = 1";

        return new CursorLoader(this,
                MountEntry.CONTENT_URI,
                projection,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
