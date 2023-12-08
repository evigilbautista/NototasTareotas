package com.NototasTareotas.photonotes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.getSystemService

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.NototasTareotas.photonotes.MyApp.Companion.CHANNEL_ID
import com.NototasTareotas.photonotes.ui.EditNote.NoteEditScreen
import com.NototasTareotas.photonotes.ui.NoteDetail.NoteDetailScreen
import com.NototasTareotas.photonotes.ui.NotesList.NotesList
import com.NototasTareotas.photonotes.ui.NotesViewModel
import com.NototasTareotas.photonotes.ui.NotesViewModelFactory
import com.NototasTareotas.photonotes.ui.createNote.CreateHWScreen
import com.NototasTareotas.photonotes.ui.createNote.CreateNoteScreen
import com.NototasTareotas.photonotes.ui.theme.PhotoNotesTheme

class MainActivity : ComponentActivity() {

    private lateinit var notesViewModel: NotesViewModel


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones de notas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal de notificaciones para notas"
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        // retrieve viewModel
        notesViewModel =
            NotesViewModelFactory(PhotoNotesApp.getDao()).create(NotesViewModel::class.java)

        setContent {



                val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Constants.NAVIGATION_NOTES_LIST
            ) {
                // Notes List
                composable(Constants.NAVIGATION_NOTES_LIST) {
                    NotesList(
                        navController,
                        notesViewModel
                    )
                }


                composable(
                    Constants.NAVIGATION_NOTE_DETAIL,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTE_ID_Argument) {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_ID_Argument)
                        ?.let { NoteDetailScreen(noteId = it, navController, notesViewModel) }
                }

                // Notes Edit
                composable(
                    Constants.NAVIGATION_NOTE_EDIT,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTE_ID_Argument) {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_ID_Argument)
                        ?.let { NoteEditScreen(noteId = it, navController, notesViewModel) }
                }

                // Create Note
                composable(Constants.NAVIGATION_NOTES_CREATE) {
                    CreateNoteScreen(
                        navController,
                        notesViewModel
                    )
                }

                // Create Homework
                composable(Constants.NAVIGATION_HW_CREATE){
                    CreateHWScreen(
                        navController,
                        notesViewModel)
                }
            }
        }
    }
}
