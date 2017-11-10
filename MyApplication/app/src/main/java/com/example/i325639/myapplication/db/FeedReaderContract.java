package com.example.i325639.myapplication.db;

import android.provider.BaseColumns;

/**
 * Created by I325639 on 11/2/2017.
 */

public class FeedReaderContract {
    private FeedReaderContract() {}
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
