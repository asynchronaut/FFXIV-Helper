package com.example.android.ffxivhelper;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.ffxivhelper.data.CollectiblesContract;
import com.example.android.ffxivhelper.data.CollectiblesContract.MountEntry;
import com.example.android.ffxivhelper.data.CollectiblesDbHelper;

public class DatabaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mCharacterIdView;
    private TextView mCharacterNameView;
    private TextView mCharacterServerView;
    private ListView mDatabaseListView;
    private CollectibleCursorAdapter mCursorAdapter;

    private final int LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        mCharacterIdView = findViewById(R.id.tv_character_id);
        mCharacterNameView = findViewById(R.id.tv_character_name);
        mCharacterServerView = findViewById(R.id.tv_character_server);
        mDatabaseListView = findViewById(R.id.lv_database);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContentUris.withAppendedId(CollectiblesContract.ProfileEntry.CONTENT_URI, 1); //TODO: don't hardcode this id
        Cursor cursor = contentResolver.query(uri, null,null,null,null);
        cursor.moveToFirst();
        String charId = cursor.getString(cursor.getColumnIndex(CollectiblesContract.ProfileEntry.COLUMN_ID));
        mCharacterIdView.setText(charId);
        String charName = cursor.getString(cursor.getColumnIndex(CollectiblesContract.ProfileEntry.COLUMN_NAME));
        mCharacterNameView.setText(charName);
        String charServer = cursor.getString(cursor.getColumnIndex(CollectiblesContract.ProfileEntry.COLUMN_SERVER));
        mCharacterServerView.setText(charServer);

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
