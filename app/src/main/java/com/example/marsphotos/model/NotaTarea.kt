/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.marsphotos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This data class defines a Mars photo which includes an ID, and the image URL.
 */


//Tipo 0 = Nota --- 1 = Tarea

@Entity
data class NotaTarea(

    @PrimaryKey(autoGenerate = true)
    var idNota: Int,
    var idUser: Int,
    var titulo: String,
    var contenido: String,
    var estatus: Int,
    var tipo: Int,
    var fecha: Long,
    var fechaModi: Long,
    var fechaCum: Long

) {
    constructor(idUser: Int, titulo: String, contenido: String, estatus: Int, tipo: Int, fecha: Long, fechaModi: Long, fechaCum: Long) :
            this(
                0,
                idUser,
                titulo,
                contenido,
                estatus,
                tipo,
                fecha,
                fechaModi,
                fechaCum
            )
}
