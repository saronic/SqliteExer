package com.li.sqliteexer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.li.sqliteexer.database.BookStoreContract;
import com.li.sqliteexer.database.MyDatabaseHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button mCreateDbButton;
    private MyDatabaseHelper mDbHelper;
    private Button mInsertButton;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private Button mQueryButton;
    private Button mReplaceButton;
    private Button xutilsButton;
    private Button mContentProviderQueryButton;
    private Button mContentProviderInsertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new MyDatabaseHelper(this, "bookstore.db", null, 3);
       mCreateDbButton = (Button) findViewById(R.id.activity_main_create_db_button);
        mCreateDbButton.setOnClickListener(this);

        mInsertButton = (Button) findViewById(
                R.id.activity_main_insert_button);
        mInsertButton.setOnClickListener(this);

        mUpdateButton = (Button) findViewById(R.id.activity_main_update_button);
        mUpdateButton.setOnClickListener(this);

        mDeleteButton = (Button) findViewById(R.id.activity_main_delete_button);
        mDeleteButton.setOnClickListener(this);

        mQueryButton = (Button) findViewById(R.id.activity_main_query_button);
        mQueryButton.setOnClickListener(this);

        mReplaceButton = (Button) findViewById(R.id.activity_main_replace_button);
        mReplaceButton.setOnClickListener(this);

        /*
        // xuitls Button.
        xutilsButton = (Button) findViewById(R.id.xutilsButtonId);
        xutilsButton.setOnClickListener(this);
        */

        mContentProviderQueryButton = (Button) findViewById(
                R.id.activity_main_content_provider_query_button);
        mContentProviderQueryButton.setOnClickListener(this);

        mContentProviderInsertButton = (Button) findViewById(
                R.id.activity_main_content_provider_insert_button);
        mContentProviderInsertButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db;
        switch (v.getId()) {
            case R.id.activity_main_create_db_button:
                mDbHelper.getReadableDatabase();
                break;
            case R.id.activity_main_insert_button:
                db = mDbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(BookStoreContract.Book.COLUNM_NAME, "The Da Vinci Code");
                cv.put("author", "Dan Brown");
                cv.put("pages", 454);
                cv.put("price", 16.96);
                db.insert("book", null, cv);
                cv.clear();
                cv.put("name", "The lost symbol");
                cv.put("author", "Dan brown");
                cv.put("pages", 510);
                cv.put("price", 19.95);
                db.insert("book", null, cv);
                cv.clear();
                cv.put("name", "The game of throne");
                cv.put("author", "martin");
                cv.put("pages", 510);
                cv.put("price", 29.95);
                db.insert("book", null, cv);
                break;
            case R.id.activity_main_update_button:
                db = mDbHelper.getWritableDatabase();
                cv = new ContentValues();
                cv.put("price", 10);
                db.update("book", cv, "name=?", new String[]{"The Da Vinci Code"});
                break;
            case R.id.activity_main_delete_button:
                db = mDbHelper.getWritableDatabase();
                int affectedRows = db.delete("book", "pages>? and price>?",
                        new String[]{"500", "20"});
                Log.d(TAG, "delete number of rows: " + affectedRows);
                break;
            case R.id.activity_main_query_button:
                db = mDbHelper.getReadableDatabase();
                Cursor cursor;
                String[] columns = new String[]{"author", "name", "pages"};
                cursor = db.query("book", columns, "pages>?", new String[]{"500"}, null, null,
                        null);
                if (cursor == null) {
                    return;
                }
                while (cursor.moveToNext()) {
                    String name = cursor.getString(1);
                    int pages = cursor.getInt(2);
                    Log.d(TAG, "name: " + name + " ;pages: " + pages);
                }
                break;
            case R.id.activity_main_replace_button:
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();

                try {
                    db.delete("book", null, null);
                    if (false) {
                        throw new NullPointerException();
                    }
                    db.execSQL("insert into book(author,name,pages) values(?,?,?)",
                            new String[]{"author", "name", "100"});
                    db.setTransactionSuccessful();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            case R.id.xutilsButtonId:
                HttpUtils http = new HttpUtils();
                http.send(HttpRequest.HttpMethod.GET,
                        "http://tapi.test.tuoguibao.com/basicapi.php?ac=1002&mobile=18562172800&password=123456",
                        new RequestCallBack<String>(){
                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
//                                testTextView.setText(current + "/" + total);
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {

                                Log.i("jasonTag",responseInfo.result);
                            }

                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Log.i("jasonTag","网络连接失败！");
                            }
                        });
            case R.id.activity_main_content_provider_query_button:
                Uri uri = Uri.parse("content://com.li.sqliteexer/book/4");
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToNext()) {
                    String bookName = cursor.getString(cursor.getColumnIndex(BookStoreContract.Book.COLUNM_NAME));
                    Toast.makeText(MainActivity.this, bookName, Toast.LENGTH_SHORT).show();
                }

            case R.id.activity_main_content_provider_insert_button:
                ContentValues cvContentProvider = new ContentValues();
                cvContentProvider.put("name", "xxxxxxxxxxxxxxxxx");
                cvContentProvider.put("pages", 600);
                Uri contentProviderInsertUri = Uri.parse("content://com.li.sqliteexer/book");
                getContentResolver().insert(contentProviderInsertUri, cvContentProvider);
        }
    }
}
