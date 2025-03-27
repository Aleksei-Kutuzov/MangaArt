package com.killerficha.mangaart.PDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class ProjectsDataBaseOpener extends SQLiteOpenHelper {
    // Данные базы данных
    private static final String DATABASE_NAME = "projects.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "projects";

    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PROJECT = "PROJECT";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DATE = "DATE";

    // Синглтон-экземпляр
    private static volatile ProjectsDataBaseOpener instance;
    private SQLiteDatabase database;

    // Приватный конструктор
    private ProjectsDataBaseOpener(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    // Потокобезопасный метод получения экземпляра
    public static synchronized ProjectsDataBaseOpener getInstance(Context context) {
        if (instance == null) {
            synchronized (ProjectsDataBaseOpener.class) {
                if (instance == null) {
                    instance = new ProjectsDataBaseOpener(context);
                }
            }
        }
        return instance;
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PROJECT + " BLOB, " +
                COLUMN_NAME + " TEXT, " +  // STRING -> TEXT (стандартный тип SQLite)
                COLUMN_DATE + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Методы работы с БД (теперь используют synchronized при необходимости)

    public synchronized long insert(byte[] project, String name, Date date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROJECT, project);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date.getTime());
        return database.insert(TABLE_NAME, null, cv);
    }

    public synchronized Cursor getAllProjects() {
        return database.query(
                TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DATE},
                null, null, null, null,
                COLUMN_DATE + " DESC"
        );
    }

    public synchronized Cursor getAllProjectsWithFormattedDate() {
        String query = "SELECT _id, NAME, " +
                "strftime('%d.%m.%Y', DATE/1000, 'unixepoch') as formatted_date " +
                "FROM " + TABLE_NAME + " ORDER BY DATE DESC";
        return database.rawQuery(query, null);
    }

    public synchronized ProjectInfo select_project_info(long id) {
        try (Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{COLUMN_NAME, COLUMN_DATE},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null)
        ) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                Date date = new Date(cursor.getLong(1));
                return new ProjectInfo(name, date);
            }
            return null;
        }
    }

    public synchronized byte[] select_project(long id) {
        try (Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{COLUMN_PROJECT},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null)
        ) {
            if (cursor.moveToFirst()) {
                return cursor.getBlob(0);
            }
            return null;
        }
    }

    @Override
    public synchronized void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        super.close();
    }
}