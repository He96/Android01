package com.he.android_1.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.he.android_1.model.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String create_map = "CREATE TABLE map (id integer primary key autoincrement, k text, v text)";
        db.execSQL(create_map);
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
        String sql = "update pages set planned=" + info.getPlanned() + " where id = " + info.getId();
        db.execSQL(sql);
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

    /**
     * 添加键值对 存在则替换key
     *
     * @param key   键
     * @param value 值
     */
    public void addMap(String key, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Map<String, String> map = getMap(key);
        if (map.size() > 0 && map.containsKey("key")) {
            String sql = "update map set v=" + value + " where key = " + key;
            db.execSQL(sql);
        } else {
            cv.put("k", key);
            cv.put("v", value);
            db.insert("map", null, cv);
        }
    }

    /**
     * 根据key获取指定的键值对
     *
     * @param key
     * @return
     */
    public Map<String, String> getMap(String key) {
        Map<String, String> map = new HashMap<>();
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "select * from map where k=?";
            Cursor cursor = db.rawQuery(sql, new String[]{key});
            while (cursor.moveToNext()) {
                String k = cursor.getString(cursor.getColumnIndex("k"));
                String v = cursor.getString(cursor.getColumnIndex("v"));
                map.put(k, v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
