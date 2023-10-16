package com.example.marsphotos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users(
    @PrimaryKey(autoGenerate = true)
    var idUser: Int,
    var nombre: String,
    var contra: String
) {
    constructor(nombre: String, contra: String) :
            this(
                0,
                nombre,
                contra
            )
}