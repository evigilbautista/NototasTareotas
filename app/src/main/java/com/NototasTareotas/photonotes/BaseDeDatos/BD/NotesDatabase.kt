package com.NototasTareotas.photonotes.BaseDeDatos.BD

import androidx.room.Database
import androidx.room.RoomDatabase
import com.NototasTareotas.photonotes.BaseDeDatos.Models.Note
import com.NototasTareotas.photonotes.BaseDeDatos.DAOs.NotesDao

@Database(entities = [
  Note::class], version = 2)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun NotesDao(): NotesDao
}