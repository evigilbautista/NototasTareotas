package com.example.marsphotos.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.marsphotos.model.Archivos

interface ArchivosDAO {
    @Insert
    fun insert(archivos: Archivos)

    @Query("SELECT * FROM archivos WHERE idArchivos = :idArchivos")
    fun getArchivos(idArchivos: Int): List<Archivos>

    @Delete
    fun deleteArchivos(archivos: Archivos)
}