package com.killerficha.mangaart.PDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.killerficha.mangaart.ProjectInstruments.Project;

import java.time.Instant;
import java.util.Date;

public class ProjectsDataBaseOpener extends SQLiteOpenHelper {
    // Данные базы данных и таблиц
    private static final String DATABASE_NAME = "projects.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "projects";
    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PROJECT = "PROJECT";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DATE = "DATE";
    // Номера столбцов
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_PROJECT = 1;
    private static final int NUM_COLUMN_NAME = 1;
    private static final int NUM_COLUMN_DATE = 2;

    public SQLiteDatabase mDataBase;

    public ProjectsDataBaseOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDataBase = this.getWritableDatabase();
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PROJECT + " BLOB, " +
                COLUMN_NAME + " STRING, " +
                COLUMN_DATE + " DATE); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insert(byte[] project, String name, Date date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROJECT, project);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date.getTime());
        return mDataBase.insert(TABLE_NAME,  null, cv);
    }

    public ProjectInfo select_project_info(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME,
                null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);
        mCursor.moveToFirst();
        byte[] project = mCursor.getBlob(NUM_COLUMN_PROJECT);
        String name = mCursor.getString(NUM_COLUMN_NAME);
        Date date = new Date(mCursor.getLong(NUM_COLUMN_DATE));
        return new ProjectInfo(name, date);
    }
}
