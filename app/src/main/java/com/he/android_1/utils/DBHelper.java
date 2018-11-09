package com.he.android_1.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.he.android_1.model.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshuang on 2018/11/9 0009.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "h.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String create_table = "CREATE TABLE pages (id integer primary key autoincrement, title text, content text, planned double)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(PageInfo pageInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", pageInfo.getTitle());
        cv.put("content", pageInfo.getContext());
        cv.put("planned", new Double("0.0"));
        db.insert("pages", null, cv);
    }

    public void update(PageInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        if (info.getPlanned() <= 80.0) {
            String sql = "update pages set planned=" + info.getPlanned() + " where id = " + info.getId();
            db.execSQL(sql);
        }
    }

    public List<PageInfo> getPageList(String orderBy) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("pages", null, null, null, null, null, orderBy, null);
        List<PageInfo> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            PageInfo info;
            for (int i = 0; i < cursor.getCount(); i++) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                double planned = cursor.getDouble(cursor.getColumnIndex("planned"));
                info = new PageInfo(id, title, content, planned);
                list.add(info);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public String queryPlanned(String sql, String[] bindArgs) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, bindArgs);
        String result = null;
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("planned"));
        }
        return result;
    }

}