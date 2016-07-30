package com.li.sqliteexer.database;

import android.provider.BaseColumns;

/**
 * Created by lsx on 2016/7/26.
 */
public class BookStoreContract {
    public static final String AUTHORITY = "com.li.sqliteexer";

    public static abstract class Book implements BaseColumns {
        public static final String TABLE_NAME = "book";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUNM_NAME = "name";
        public static final String COLUN_PAGES = "pages";
        public static final String COLUN_PRICE = "price";
    }
}
