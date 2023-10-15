package com.example.marsphotos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Archivos(
    @PrimaryKey(autoGenerate = true)
    var idArchivos: Int,
    var idNota: Int,
    var url: String,
    var ruta: String,
    var descripcion: String,
    var tipo: Int
) {
    constructor(idNota: Int, url: String, ruta: String, descripcion: String, tipo: Int) :
            this(
                0,
                idNota,
                url,
                ruta,
                descripcion,
                tipo
            )
}

