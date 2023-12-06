package com.NototasTareotas.photonotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.NototasTareotas.photonotes.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Anotación @Entity indica que esta clase representa una entidad de Room (tabla en la base de datos)
@Entity(tableName = Constants.TABLE_NAME)
data class Note(
    // Anotación @PrimaryKey indica que 'id' es la clave primaria y puede ser generada automáticamente
    @PrimaryKey(autoGenerate = true) val id: Int,

    // Anotación @ColumnInfo especifica el nombre de la columna en la base de datos
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "dateUpdated") val dateUpdated: String = getDateCreated(),
    @ColumnInfo(name = "imageUri") val imageUri: String? = null,
    @ColumnInfo(name = "VideoUri") val videoUri: String? = null,
    @ColumnInfo(name = "AudioUri") val audioUri: String? = null,
    @ColumnInfo(name="tipo") val tipo: Int
)

// Función auxiliar para obtener la fecha y hora actual formateada
fun getDateCreated(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

// Función de extensión para obtener el día de la fecha de actualización de la nota
fun Note.getDay(): String {
    // Verificar si la fecha de actualización está vacía
    if (this.dateUpdated.isEmpty()) return ""

    // Convertir la fecha de actualización de String a LocalDate y formatearla
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(this.dateUpdated, formatter).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}
