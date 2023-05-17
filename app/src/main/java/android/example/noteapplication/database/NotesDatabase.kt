package android.example.noteapplication.database

import android.content.Context
import android.example.noteapplication.entities.Notes
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class NotesDatabase : RoomDatabase() {

    companion object {
        var notesDatabase: NotesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NotesDatabase {
            if (notesDatabase != null) {
                notesDatabase = Room.databaseBuilder(
                    context
                    , NotesDatabase::class.java
                    , "notes.db"
                ).build()
            }
            return notesDatabase!!
        }
    }
    abstract fun noteDao():Notes
}