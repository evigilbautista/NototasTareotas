package com.example.marsphotos.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.marsphotos.model.Recordatorios

interface RecordatoriosDAO {
    @Insert
    fun insert(recordatorios: Recordatorios)

    @Query("SELECT * FROM recordatorios WHERE idNota = :idNota")
    fun getRecordatorios(idNota: Int): List<Recordatorios>

    @Delete
    fun deleteRecordatorios(recordatorios: Recordatorios)
}