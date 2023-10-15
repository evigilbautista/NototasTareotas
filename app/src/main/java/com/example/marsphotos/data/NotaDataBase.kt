package com.example.marsphotos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.marsphotos.model.NotaTarea
import com.example.marsphotos.model.Archivos
import com.example.marsphotos.model.Recordatorios
import com.example.marsphotos.model.Users


@Database(entities = [Archivos::class, NotaTarea::class, Recordatorios::class, Users::class], version = 1, exportSchema = false)
abstract class NotaDataBase : RoomDatabase(){
    abstract fun notaTareaDAO(): NotaTareaDAO
    abstract fun archivosDao(): ArchivosDAO
    abstract fun recordatoriosDao(): RecordatoriosDAO
    abstract  fun usersDAO(): UsersDAO
    companion object {
        private var INSTANCE: NotaDataBase? = null
        fun getDatabase(context: Context): NotaDataBase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,NotaDataBase::class.java, "note")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }

}