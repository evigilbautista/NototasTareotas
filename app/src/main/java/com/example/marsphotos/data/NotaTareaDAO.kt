package com.example.marsphotos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.marsphotos.model.NotaTarea


@Dao
interface NotaTareaDAO {
    // Obtiene toda la información de una Nota o Tarea
    @Query  ("SELECT * FROM NotaTarea")
    fun getAll(): List<NotaTarea>
    // Obtener info de nota = 0  Nota abierta
    // Obeter info de tarea = 1  Tarea abierta
    @Query("SELECT titulo, contenido, estatus, fecha, fechaModi, fechaCum FROM NotaTarea WHERE tipo=:tipo")
    fun getTarea(tipo: Int): NotaTarea
    // Ver desde fuera la nota
    @Query("SELECT titulo, estatus FROM NotaTarea WHERE tipo=:tipo")
    fun getLista(tipo: Int): NotaTarea
    //Borrar
    @Delete
    fun delete(notaTarea: NotaTarea)
    //Insertar
    @Insert
    fun insertAll(vararg notaTarea: NotaTarea)

    //Actualizar

}