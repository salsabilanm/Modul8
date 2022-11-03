package com.example.tugasmodul7.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tugasmodul7.database.DAONote;
import com.example.tugasmodul7.database.Note;
import com.example.tugasmodul7.database.NoteRoomDB;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private final DAONote mDaoNotes;
    private final ExecutorService executorService;
    public NoteRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDB db = NoteRoomDB.getDatabase(application);
        mDaoNotes = db.daoNote();
    }
    public LiveData<List<Note>> getAllNotes() {
        return mDaoNotes.getAllNotes();
    }
    public void insert(final Note note) {
        executorService.execute(() -> mDaoNotes.insert(note));
    }
    public void delete(final Note note){
        executorService.execute(() -> mDaoNotes.delete(note));
    }
    public void update(final Note note){
        executorService.execute(() -> mDaoNotes.update(note));
    }
}
