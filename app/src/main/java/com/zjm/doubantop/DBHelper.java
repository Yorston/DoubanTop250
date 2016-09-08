package com.zjm.doubantop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by B on 2016/9/7.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieCollectionDB";
    private static final String TABLE_NAME = "MyCollection";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }//148

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "_id INTEGER NOT NULL PRIMARY KEY," +
                "content VARCHAR(100) NOT NULL," +
                "imageurl VARCHAR(150) NOT NULL," +
                "title VARCHAR(30) NOT NULL," +
                "coverurl VARCHAR(150) NOT NULL" + ")");
    }

    public void insertDate(String content, String imageurl, String title, String coverurl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("imageurl", imageurl);
        values.put("title", title);
        values.put("coverurl", coverurl);
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor RetAllCollect(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return cursor;
    }

    public void delDate(String Title){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {String.valueOf(Title)};
        db.delete(TABLE_NAME, "title=?",args);
    }

    public boolean checkExist(String Title){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {String.valueOf(Title)};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"title"}, "title=?", args, null, null, null,null);
        if(cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndex("title"));
            if(!title.equals("")){
                return true;
            }
        }
        return false;
    }

}
