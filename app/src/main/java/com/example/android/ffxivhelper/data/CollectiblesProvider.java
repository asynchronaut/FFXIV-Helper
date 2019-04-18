package com.example.android.ffxivhelper.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.android.ffxivhelper.R;
import com.example.android.ffxivhelper.data.CollectiblesContract.MountEntry;
import com.example.android.ffxivhelper.data.CollectiblesContract.ProfileEntry;


public class CollectiblesProvider extends ContentProvider {

    public static final String LOG_TAG = CollectiblesProvider.class.getSimpleName();

    private CollectiblesDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int PROFILES = 10;
    private static final int PROFILE = 11;
    private static final int MOUNTS = 100;
    private static final int MOUNT = 101;

    static {
        sUriMatcher.addURI(CollectiblesContract.CONTENT_AUTHORITY,CollectiblesContract.PATH_PROFILES,PROFILES);
        sUriMatcher.addURI(CollectiblesContract.CONTENT_AUTHORITY,CollectiblesContract.PATH_PROFILES + "/#",PROFILE);
        sUriMatcher.addURI(CollectiblesContract.CONTENT_AUTHORITY,CollectiblesContract.PATH_MOUNTS,MOUNTS);
        sUriMatcher.addURI(CollectiblesContract.CONTENT_AUTHORITY, CollectiblesContract.PATH_MOUNTS + "/#",MOUNT);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new CollectiblesDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                cursor = db.query(
                        ProfileEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PROFILE:
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(
                        ProfileEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOUNTS:
                cursor = db.query(
                        MountEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOUNT:
                selection = MountEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(
                        MountEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException(
                        getContext().getResources().getString(R.string.error_query_uri) + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch(match) {
            case PROFILES:
                return ProfileEntry.CONTENT_LIST_TYPE;
            case PROFILE:
                return ProfileEntry.CONTENT_ITEM_TYPE;
            case MOUNTS:
                return MountEntry.CONTENT_LIST_TYPE;
            case MOUNT:
                return MountEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(
                        getContext().getResources().getString(R.string.error_get_type_uri) + uri);
        }    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                return updateProfiles(uri, values, selection, selectionArgs);
            case PROFILE:
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProfiles(uri, values, selection, selectionArgs);
            case MOUNTS:
                return updateMounts(uri,values,selection,selectionArgs);
            case MOUNT:
                selection = MountEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateMounts(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        getContext().getResources().getString(R.string.error_update_uri) + uri);
        }
    }

    private int updateProfiles(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated;

        rowsUpdated = db.update(ProfileEntry.TABLE_NAME,values,selection,selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        else {
            Log.e(LOG_TAG, getContext().getResources().getString(R.string.error_update_fail));
            Toast.makeText(getContext(), R.string.error_update_fail, Toast.LENGTH_SHORT).show();
        }
        return rowsUpdated;
    }

    private int updateMounts(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated;

        rowsUpdated = db.update(MountEntry.TABLE_NAME,values,selection,selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        else {
            Log.e(LOG_TAG, getContext().getResources().getString(R.string.error_update_fail));
            Toast.makeText(getContext(), R.string.error_update_fail, Toast.LENGTH_SHORT).show();
        }
        return rowsUpdated;
    }
}
