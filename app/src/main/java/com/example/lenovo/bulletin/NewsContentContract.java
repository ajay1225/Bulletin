package com.example.lenovo.bulletin;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContentContract {
    static final String AUTHORITY = "com.example.lenovo.bulletin";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_TASKS = "news";
    static final class NewsEntry implements BaseColumns {

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        static final String Tablename = "news";

        static final String COL_1 = "title";
        static final String COL_2 = "news_poster";
    }
}
