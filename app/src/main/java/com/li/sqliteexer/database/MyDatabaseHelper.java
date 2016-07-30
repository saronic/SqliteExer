package com.li.sqliteexer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.li.sqliteexer.database.BookStoreContract.Book;

/**
 * Created by lsx on 2016/7/26.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_BOOK = "create table " + Book.TABLE_NAME +"(" +
            Book._ID + " integer primary key autoincrement," +
            Book.COLUMN_AUTHOR + " text," +
            Book.COLUN_PRICE + " real," +
            Book.COLUN_PAGES +" int," +
            Book.COLUNM_NAME + " text)";

    public static final String CREATE_CATEGORY = "create table category (" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "table:book created........", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists book");
        db.execSQL("drop table if exists category");
        onCreate(db);
    }
}
