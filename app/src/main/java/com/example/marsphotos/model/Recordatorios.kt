package com.example.marsphotos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recordatorios (
    @PrimaryKey(autoGenerate = true)
    var idRecordaorios: Int,
    var idNota: Int,
    var fechaRecordatorio: Long
) {
    constructor(idNota: Int, fechaRecordatorio: Long) :
            this(
                0,
                idNota,
                fechaRecordatorio
            )
}
