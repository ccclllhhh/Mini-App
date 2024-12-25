package com.example.noteapp.dao;

import android.view.ViewDebug;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.noteapp.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("select * from note order by id desc")
    List<Note> getAllNotes();

//    // Query to get all notes by user id
//    @Query("SELECT * FROM note WHERE userId = :userId ORDER BY id DESC")
//    List<Note> getNotesByUserId(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Query("SELECT * FROM note WHERE user_id = :userId")
    List<Note> getNotesByUserId(int userId);

    @Delete
    void deleteNote(Note note);
}
