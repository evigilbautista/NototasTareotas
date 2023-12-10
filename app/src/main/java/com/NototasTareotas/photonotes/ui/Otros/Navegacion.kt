package com.NototasTareotas.photonotes.ui.Otros

import com.NototasTareotas.photonotes.BaseDeDatos.Models.Note

object Navegacion {
    const val NAVIGATION_NOTES_LIST = "notesList"
    const val NAVIGATION_HW_LIST = "hwList"
    const val NAVIGATION_NOTES_CREATE = "notesCreated"
    const val NAVIGATION_HW_CREATE = "hwCreated"
    const val NAVIGATION_NOTE_DETAIL = "noteDetail/{noteId}"
    const val NAVIGATION_HW_DETAIL = "hwDetail/{noteId}"
    const val NAVIGATION_NOTE_EDIT= "noteEdit/{noteId}"
    const val NAVIGATION_HW_EDIT = "hwEdit/{noteId}"
    const val NAVIGATION_NOTE_ID_Argument = "noteId"
    const val TABLE_NAME = "Notes"
    const val DATABASE_NAME = "NotesDatabase"
    const val idCanal = "Terminaste los deberes"

    fun noteDetailNavigation(noteId : Int) = "noteDetail/$noteId"
    fun noteEditNavigation(noteId : Int) = "noteEdit/$noteId"

    fun HWEditNavigation(noteId : Int) = "noteEdit/$noteId"


    fun List<Note>?.orPlaceHolderList(): List<Note> {
        fun placeHolderList(): List<Note> {
            return listOf(Note(id = 0, title = "No Notas encontradas", note = "Por favor crea una nota.", dateUpdated = "", tipo = 1))
        }
        return if (this != null && this.isNotEmpty()){
            this
        } else placeHolderList()
    }

    val noteDetailPlaceHolder = Note(note = "No se encuentran detalles de la nota", id = 0, title = "No se encuentra detalles de la nota", tipo = 1)
}