package com.capfloat.taskapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.capfloat.taskapp.model.NewsModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "NewsDatabase.db";
    private final static int VERSION = 1;
    private final String TABLE_NAME = "News";
    private final String COLUMN_AUTHOR = "author";
    private final String COLUMN_TITLE = "title";
    private final String COLUMN_DESCRIPTION = "description";


    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("create table "+ TABLE_NAME + " ( id"+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + COLUMN_TITLE + " text,"
                    + COLUMN_AUTHOR + " text,"
                    + COLUMN_DESCRIPTION + " text)");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void insertData(String author, String title, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AUTHOR,author);
        contentValues.put(COLUMN_TITLE,title);
        contentValues.put(COLUMN_DESCRIPTION,description);
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
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                NewsModel newsModel = new NewsModel(author,title,description);
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

    public int getItemCount(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select count(*) from "+TABLE_NAME,null);
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        if (cursor != null) {
            cursor.close();
        }
        return count;
    }
}

