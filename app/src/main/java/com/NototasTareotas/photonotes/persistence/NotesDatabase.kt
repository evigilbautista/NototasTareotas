package com.NototasTareotas.photonotes.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.NototasTareotas.photonotes.model.Note

@Database(entities = [
  Note::class], version = 2)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun NotesDao(): NotesDao
}