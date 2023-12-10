package com.NototasTareotas.photonotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.NototasTareotas.photonotes.ui.Crear.CreateHWScreen
import com.NototasTareotas.photonotes.ui.Crear.CreateNoteScreen
import com.NototasTareotas.photonotes.ui.Detalles.HWDetailScreen
import com.NototasTareotas.photonotes.ui.Detalles.NoteDetailScreen
import com.NototasTareotas.photonotes.ui.Editar.HWEditScreen
import com.NototasTareotas.photonotes.ui.Editar.NoteEditScreen
import com.NototasTareotas.photonotes.ui.Lista.HWList
import com.NototasTareotas.photonotes.ui.Lista.NotesList
import com.NototasTareotas.photonotes.ui.NotesViewModel
import com.NototasTareotas.photonotes.ui.NotesViewModelFactory
import com.NototasTareotas.photonotes.ui.Otros.Navegacion

class MainActivity : ComponentActivity() {


    private lateinit var notesViewModel: NotesViewModel






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // retrieve viewModel
        notesViewModel =
            NotesViewModelFactory(PhotoNotesApp.getDao()).create(NotesViewModel::class.java)

        setContent {



                val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Navegacion.NAVIGATION_NOTES_LIST
            ) {
                // Notes List
                composable(Navegacion.NAVIGATION_NOTES_LIST) {
                    NotesList(
                        navController,
                        notesViewModel
                    )
                }
                composable(Navegacion.NAVIGATION_HW_LIST) {
                    HWList(
                        navController,
                        notesViewModel
                    )
                }

                composable(
                    Navegacion.NAVIGATION_NOTE_DETAIL,
                    arguments = listOf(navArgument(Navegacion.NAVIGATION_NOTE_ID_Argument) {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getInt(Navegacion.NAVIGATION_NOTE_ID_Argument)
                        ?.let { NoteDetailScreen(noteId = it, navController, notesViewModel) }
                }

                composable(
                    Navegacion.NAVIGATION_HW_DETAIL,
                    arguments = listOf(navArgument(Navegacion.NAVIGATION_NOTE_ID_Argument) {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getInt(Navegacion.NAVIGATION_NOTE_ID_Argument)
                        ?.let { HWDetailScreen(noteId = it, navController, notesViewModel) }
                }

                // Notes Edit
                composable(
                    Navegacion.NAVIGATION_NOTE_EDIT,
                    arguments = listOf(navArgument(Navegacion.NAVIGATION_NOTE_ID_Argument) {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getInt(Navegacion.NAVIGATION_NOTE_ID_Argument)
                        ?.let { NoteEditScreen(noteId = it, navController, notesViewModel) }
                }

                composable(
                    Navegacion.NAVIGATION_HW_EDIT,
                    arguments = listOf(navArgument(Navegacion.NAVIGATION_NOTE_ID_Argument) {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getInt(Navegacion.NAVIGATION_NOTE_ID_Argument)
                        ?.let { HWEditScreen(noteId = it, navController, notesViewModel) }
                }

                // Create Note
                composable(Navegacion.NAVIGATION_NOTES_CREATE) {
                    val context = LocalContext.current
                    CreateNoteScreen(

                        navController,
                        notesViewModel,

                    )
                }

                // Create Homework
                composable(Navegacion.NAVIGATION_HW_CREATE){
                    CreateHWScreen(
                        navController,
                        notesViewModel)
                }
            }
        }
    }
}
