package com.example.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.noteapp.Models.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class DataBasehalper extends SQLiteOpenHelper {
    public static final String TABLE_NAME_NOTE = "note";
    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DESCRIPTION = "description";
    public static final String COLUMN_NOTE_DATE = "date";

    public DataBasehalper(@Nullable Context context) {
        super(context, "NoteApp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String notesTable = "CREATE TABLE " + TABLE_NAME_NOTE + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOTE_TITLE + " TEXT NOT NULL," +
                COLUMN_NOTE_DESCRIPTION + " TEXT," +
                COLUMN_NOTE_DATE + " TEXT NOT NULL)";

        db.execSQL(notesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addNoteRow(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE_TITLE, noteModel.getTitle());
        cv.put(COLUMN_NOTE_DESCRIPTION, noteModel.getDescription());
        cv.put(COLUMN_NOTE_DATE, noteModel.getDate());

        boolean inserted = db.insert(TABLE_NAME_NOTE, null, cv) > 0;
        db.close();

        return inserted;
    }

    public boolean deleteNoteRow(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean deleted = db.delete(TABLE_NAME_NOTE, COLUMN_NOTE_ID + " = " + noteModel.getId(), null) > 0;
        db.close();
        return deleted;
    }

    public List<NoteModel> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<NoteModel> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_NOTE, null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            String date = cursor.getString(3);
            list.add(new NoteModel(id, title, desc, date));
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean updateNoteRow(NoteModel newNoteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE_TITLE, newNoteModel.getTitle());
        cv.put(COLUMN_NOTE_DESCRIPTION, newNoteModel.getDescription());
        cv.put(COLUMN_NOTE_DATE, newNoteModel.getDate());

        boolean deleted = db.update(TABLE_NAME_NOTE, cv,COLUMN_NOTE_ID + " = " + newNoteModel.getId(), null) > 0;
        db.close();
        return deleted;
    }

    public List<NoteModel> searchNotesByTitle(String searchText) {
        SQLiteDatabase db = this.getReadableDatabase();
        searchText = '%' + searchText + '%';
        List<NoteModel> list = new ArrayList<>();
        Log.d("database", "hello");
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_NOTE + " WHERE " +COLUMN_NOTE_TITLE+ " LIKE ?" , new String[] {searchText })) {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String desc = cursor.getString(2);
                String date = cursor.getString(3);
                list.add(new NoteModel(id, title, desc, date));
            }

            cursor.close();
        }
        db.close();

        return list;
    }

}
