package com.NototasTareotas.photonotes.ui

import androidx.lifecycle.*
import com.NototasTareotas.photonotes.model.Note
import com.NototasTareotas.photonotes.persistence.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel que maneja la lógica de negocio relacionada con las notas
class NotesViewModel(private val db: NotesDao, ) : ViewModel() {

    // LiveData que contiene la lista de notas, actualizado automáticamente por Room
    val notes: LiveData<List<Note>> = db.getNotes()

    // Función para eliminar una nota
    fun deleteNotes(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.deleteNote(note)
        }
    }

    // Función para actualizar una nota
    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.updateNote(note)
        }
    }

    // Función para crear una nueva nota
    fun createNote(
        title: String,
        note: String,
        image: String? = null,
        video: String? = null,
        audio: String? = null,
        tipo: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // Insertar una nueva nota en la base de datos
            //.insertNote(Note(title = title, note = note, imageUri = image))
            db.insertNote(Note(id = 0, title = title, note = note, imageUri = image, videoUri = video, audioUri = audio, tipo = 2))
        }
    }

    // Función suspendida para obtener una nota por su ID
    suspend fun getNote(noteId: Int): Note? {
        return db.getNoteById(noteId)
    }
}

// Factoría para crear instancias de NotesViewModel
class NotesViewModelFactory(
    private val db: NotesDao, // Acceso a la base de datos a través del DAO
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Crear una instancia de NotesViewModel con la base de datos proporcionada
        return NotesViewModel(db = db) as T
    }
}
