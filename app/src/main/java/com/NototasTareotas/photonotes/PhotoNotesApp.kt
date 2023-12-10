package com.NototasTareotas.photonotes
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.room.Room
import com.NototasTareotas.photonotes.BaseDeDatos.BD.NotesDatabase
import com.NototasTareotas.photonotes.BaseDeDatos.DAOs.NotesDao
import com.NototasTareotas.photonotes.ui.Otros.Navegacion

class PhotoNotesApp : Application() {

    private var db: NotesDatabase? = null


    init {
        instance = this
    }

    private fun getDb(): NotesDatabase {
        return if (db != null) {
            db!!
        } else {
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                NotesDatabase::class.java, Navegacion.DATABASE_NAME
            ).fallbackToDestructiveMigration()// remove in prod
                .build()
            db!!
        }
    }


    companion object {
        private var instance: PhotoNotesApp? = null

        fun getDao(): NotesDao {
            return instance!!.getDb().NotesDao()
        }

        fun getUriPermission(uri: Uri) {
            instance!!.applicationContext.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

    }


}

