package com.he.android_1.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.he.android_1.model.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class SqlConn {

    private static SqlConn sqlConn = null;
    private SQLiteDatabase db;

    private SqlConn() {
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.he.db/database/page.db", null);
        String sql = "select count(*)  from sqlite_master where type='table' and name = ?";
        String[] whereArgs = {"pages"};
        Cursor cursor = db.rawQuery(sql, whereArgs);
        if (cursor.getCount() < 1) {
            createTable();
        }
    }


    public static SqlConn getInstance() {
        if (sqlConn == null) {
            sqlConn = new SqlConn();
        }
        return sqlConn;
    }

    public void createTable() {
        String create_table = "CREATE TABLE `pages` (`id` int(32) NOT NULL, `title` varchar(512) DEFAULT NULL, `content` text," +
                " `planned` decimal(2,1) DEFAULT NULL," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
        db.execSQL(create_table);
    }

    public void insert(PageInfo pageInfo) {
        ContentValues cv = new ContentValues();
        cv.put("title", pageInfo.getTitle());
        cv.put("content", pageInfo.getContext());
        cv.put("planned", new Double("0.0"));
        db.insert("pages", null, cv);
    }

    public void update(PageInfo info) {
        String sql = "update pages set planned=" + info.getPlanned() + "where id = " + info.getId();
        db.execSQL(sql);

    }

    public List<PageInfo> getPageList() {
        Cursor cursor = db.query("pages", null, null, null, null, null, null, null);
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


}
