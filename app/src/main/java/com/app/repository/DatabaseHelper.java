package com.app.repository;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(DB.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DB.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(DB.COLUMN_NOTE, note);

        // insert row
        long id = db.insert(DB.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public DB getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DB.TABLE_NAME,
                new String[]{DB.COLUMN_ID, DB.COLUMN_NOTE, DB.COLUMN_TIMESTAMP},
                DB.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        DB dbObj = new DB(
                cursor.getInt(cursor.getColumnIndex(DB.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DB.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(DB.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return dbObj;
    }

    public List<DB> getAllNotes() {
        List<DB> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DB.TABLE_NAME + " ORDER BY " +
                DB.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DB note = new DB();
                note.setId(cursor.getInt(cursor.getColumnIndex(DB.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(DB.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(DB.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + DB.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(DB note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_NOTE, note.getNote());

        // updating row
        return db.update(DB.TABLE_NAME, values, DB.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(DB note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB.TABLE_NAME, DB.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
