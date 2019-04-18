package com.example.android.ffxivhelper.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class CollectiblesContract  {

    public static final String CONTENT_AUTHORITY = "com.example.android.ffxivhelper";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PROFILES = "profiles";
    public static final String PATH_MOUNTS = "mounts";

    public static final class ProfileEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PROFILES);

        public static final String TABLE_NAME = "profiles";
        public static final String _Id = BaseColumns._ID;
        public static final String COLUMN_ID = "char_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVER = "server";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;

    }

    public static final class MountEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MOUNTS);

        public static final String TABLE_NAME = "mounts";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_CHAR1 = "char1";
        public static final String COLUMN_CHAR2 = "char2";
        public static final String COLUMN_CHAR3 = "char3";
        public static final String COLUMN_CHAR4 = "char4";
        public static final String COLUMN_CHAR5 = "char5";
        public static final String COLUMN_CHAR6 = "char6";
        public static final String COLUMN_CHAR7 = "char7";
        public static final String COLUMN_CHAR8 = "char8";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOUNTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOUNTS;
    }
}
