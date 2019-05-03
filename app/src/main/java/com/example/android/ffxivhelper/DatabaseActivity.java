package com.example.android.ffxivhelper;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.ffxivhelper.data.CollectiblesContract;
import com.example.android.ffxivhelper.data.CollectiblesContract.MountEntry;
import com.example.android.ffxivhelper.data.CollectiblesDbHelper;
import com.example.android.ffxivhelper.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class DatabaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mCharacterIdView;
    private TextView mCharacterNameView;
    private TextView mCharacterServerView;
    private ListView mDatabaseListView;
    private CollectibleCursorAdapter mCursorAdapter;
    private String mCharacterId;

    private final int LOADER_ID = 100;

    private final long TIMER_REFRESH_MINS = 1;
    private final long TIMER_REFRESH_MILLIS =  TIMER_REFRESH_MINS * 60 * 1000;

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

        mCharacterId = cursor.getString(cursor.getColumnIndex(CollectiblesContract.ProfileEntry.COLUMN_ID));
        long timeUpdated = cursor.getLong(cursor.getColumnIndex(CollectiblesContract.ProfileEntry.COLUMN_UPDATED)); // TODO: Not sure if this handles nulls
        long currentTime = System.currentTimeMillis();
        if ((currentTime - timeUpdated) > TIMER_REFRESH_MILLIS) {
            URL xivapiSearchUrl = NetworkUtils.buildCharacterUrl(mCharacterId);
            new CharacterQueryTask().execute(xivapiSearchUrl);
        }

        mCharacterIdView.setText(mCharacterId);
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

    public class CharacterQueryTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
            Log.d("QUERY_TASK", "New query");
        }

        @Override
        protected JSONObject doInBackground(URL... params) {
            URL searchUrl = params[0];
            String xivapiSearchResults = null;
            JSONObject jsonResults = null;
            try {
                xivapiSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                jsonResults = new JSONObject(xivapiSearchResults);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                Log.e("FAILED", "Json parsing error: " + e.getMessage());
            }
            return jsonResults;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResults) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.d("QUERY_TASK", "Query complete");
            if (jsonResults != null && !jsonResults.equals("")) {
                //showJsonDataView();
                CollectibleObject[] results = null;
                ContentResolver contentResolver = getContentResolver();
                Uri uri = null;
                String characterName = "";
                String characterServer = "";
                try {
                    JSONObject characterData = jsonResults.getJSONObject("Character");
                    characterName = characterData.getString("Name");
                    characterServer = characterData.getString("Server");
                    JSONArray mountData = characterData.getJSONArray("Mounts");
                    Log.d("MOUNTS", String.valueOf(mountData.length()) + " mounts found.");
                    results = new CollectibleObject[mountData.length()];

                    for (int i = 0; i < mountData.length(); i++) {
                        String mountId = mountData.getString(i);
                        Log.d("MOUNTS", "Mount " + mountId + " located.");
                        results[i] = new CollectibleObject(mountId,0); //TODO: Pass correct type

                        uri = ContentUris.withAppendedId(
                                CollectiblesContract.MountEntry.CONTENT_URI,
                                Integer.parseInt(mountId));
                        ContentValues values = new ContentValues();
                        values.put(CollectiblesContract.MountEntry.COLUMN_CHAR1, 1);
                        contentResolver.update(uri, values, null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (results == null) {
                    Log.d("MOUNTS", "No mounts found.");
                }
                else {
                    uri = ContentUris.withAppendedId(CollectiblesContract.ProfileEntry.CONTENT_URI, 1); //TODO: don't hardcode this id
                    Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                    cursor.moveToFirst();
                    int char_id = Integer.parseInt(mCharacterId);
                    long currentTime = System.currentTimeMillis();

                    ContentValues values = new ContentValues();
                    values.put(CollectiblesContract.ProfileEntry.COLUMN_ID,char_id);
                    values.put(CollectiblesContract.ProfileEntry.COLUMN_NAME, characterName);
                    values.put(CollectiblesContract.ProfileEntry.COLUMN_SERVER, characterServer);
                    values.put(CollectiblesContract.ProfileEntry.COLUMN_UPDATED, currentTime);

                    contentResolver.update(uri,values,null,null);
                }
            } else {
                //showErrorMessage();
            }
        }
    }


}
