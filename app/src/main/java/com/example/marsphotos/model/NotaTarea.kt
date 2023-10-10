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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsApi


/**
 * This data class defines a Mars photo which includes an ID, and the image URL.
 */
@Serializable
data class NotaTarea(

@SerialName("id") val id: Int,
@SerialName("titulo") val titulo: String,
@SerialName("contenido") val contenido: String,
@SerialName("estatus") val estatus: Int,
@SerialName("tipo") val tipo: Int,
@SerialName("fecha") val fecha: String,
@SerialName("fechaModi") val fechaModi: String,
@SerialName("fechaCum") val fechaCum: String

) {
    override fun toString(): String {
        return """
            |NotaTarea:
            |ID: $id
            |Título: $titulo
            |Contenido: $contenido
            |Estatus: $estatus
            |Tipo: $tipo
            |Fecha: $fecha
            |Fecha de Modificación: $fechaModi
            |Fecha de Cumplimiento: $fechaCum
        """.trimMargin()
    }
}
