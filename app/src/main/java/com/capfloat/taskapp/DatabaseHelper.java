package com.capfloat.taskapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "NewsDatabase.db";
    private final static int VERSION = 1;
    private final String TABLE_NAME = "News";
    private final String COLUMN_AUTHOR = "author";
    private final String COLUMN_TITLE = "title";


    DatabaseHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("create table "+ TABLE_NAME + " ( id"+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + COLUMN_TITLE + " text,"
                    + COLUMN_AUTHOR + " text)");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void insertData(String author, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AUTHOR,author);
        contentValues.put(COLUMN_TITLE,title);
        db.insert(TABLE_NAME,null,contentValues);
    }

    public ArrayList<NewsModel> getPaginatedData(int limit, int offset){
        Cursor cursor =null;
        try {
            ArrayList<NewsModel> newsModelArrayList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            cursor =db.rawQuery("select * from " + TABLE_NAME + " ORDER BY id LIMIT " +limit
                    + " OFFSET " + offset,null );
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                NewsModel newsModel = new NewsModel(title,author);
                newsModelArrayList.add(newsModel);
            }
            return newsModelArrayList;
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }
        finally {
            if (cursor!=null){
                cursor.close();
            }
        }
    }
}
