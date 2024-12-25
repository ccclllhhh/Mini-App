package com.example.noteapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.noteapp.dao.NoteDao;
import com.example.noteapp.dao.UserDao;
import com.example.noteapp.entities.Note;
import com.example.noteapp.entities.User;

//@Database(entities = Note.class,version = 1,exportSchema = false)
//public abstract class NoteDatabase extends RoomDatabase {
//    private static NoteDatabase noteDatabase;
//
//    public static NoteDatabase getNoteDatabase(Context context){
//        if(noteDatabase == null){
//            synchronized (NoteDatabase.class){
//                if(noteDatabase == null){
//                    noteDatabase = Room.databaseBuilder(context,NoteDatabase.class,"note").build();
//                }
//            }
//        }
//        return noteDatabase;
//    }
//
//    public abstract NoteDao noteDao();
//}

@Database(entities = {Note.class, User.class}, version = 3, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase noteDatabase;

    public static NoteDatabase getNoteDatabase(Context context) {
        if (noteDatabase == null) {
            synchronized (NoteDatabase.class) {
                if (noteDatabase == null) {
                    noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, "note")
                            .addMigrations(MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return noteDatabase;
    }

    public abstract NoteDao noteDao();
    public abstract UserDao userDao();

    // Migration from 1 to 2, if you want to add a new column
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE note ADD COLUMN user_id INTEGER DEFAULT 0 NOT NULL");
        }
    };
}