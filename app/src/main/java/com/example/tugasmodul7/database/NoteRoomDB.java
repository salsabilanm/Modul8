package com.example.tugasmodul7.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDB extends RoomDatabase {
    public abstract DAONote daoNote();
    private static volatile NoteRoomDB INSTANCE;
    public static NoteRoomDB getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (NoteRoomDB.class){
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(),
                                NoteRoomDB.class, "note_db").build();
            }
        }
        return INSTANCE;
    }
}