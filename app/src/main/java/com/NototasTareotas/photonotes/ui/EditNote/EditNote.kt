package com.NototasTareotas.photonotes.ui.EditNote

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.NototasTareotas.photonotes.Constants
import com.NototasTareotas.photonotes.PhotoNotesApp
import com.NototasTareotas.photonotes.R
import com.NototasTareotas.photonotes.model.Note
import com.NototasTareotas.photonotes.ui.GenericAppBar
import com.NototasTareotas.photonotes.ui.NotesList.NotesFab
import com.NototasTareotas.photonotes.ui.NotesViewModel
import com.NototasTareotas.photonotes.ui.theme.PhotoNotesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditScreen(noteId: Int, navController: NavController, viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()
    val note = remember {
        mutableStateOf(Constants.noteDetailPlaceHolder)
    }

    val currentNote = remember { mutableStateOf(note.value.note) }
    val currentTitle = remember { mutableStateOf(note.value.title) }
    val currentPhotos = remember { mutableStateOf(note.value.imageUri) }
    val currentVideo = remember { mutableStateOf(note.value.videoUri) }
    val currentAudio = remember { mutableStateOf(note.value.audioUri) }
    val saveButtonState = remember { mutableStateOf(false) }

    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            PhotoNotesApp.getUriPermission(uri)
        }
        currentPhotos.value = uri.toString()
        if (currentPhotos.value != note.value.imageUri) {
            saveButtonState.value = true
        }
    }

    val getVideoRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            PhotoNotesApp.getUriPermission(uri)
        }
        currentVideo.value = uri.toString()
        if (currentVideo.value != note.value.videoUri) {
            saveButtonState.value = true
        }
    }

    val getAudioRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            PhotoNotesApp.getUriPermission(uri)
        }
        currentAudio.value = uri.toString()
        if (currentAudio.value != note.value.audioUri) {
            saveButtonState.value = true
        }
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: Constants.noteDetailPlaceHolder
            currentNote.value = note.value.note
            currentTitle.value = note.value.title
            currentPhotos.value = note.value.imageUri
            currentVideo.value = note.value.videoUri
            currentAudio.value = note.value.audioUri
        }
    }

    PhotoNotesTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Editar Nota",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.save_note),
                                tint = Color.Black,
                            )
                        },
                        onIconClick = {
                            viewModel.updateNote(
                                Note(
                                    id = note.value.id,
                                    note = currentNote.value,
                                    title = currentTitle.value,
                                    imageUri = currentPhotos.value,
                                    videoUri = currentVideo.value,
                                    audioUri = currentAudio.value
                                )
                            )
                            navController.popBackStack()
                        },
                        iconState = saveButtonState
                    )
                },
                floatingActionButton = {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Agregar botón para imagen
                        NotesFab(
                            contentDescription = stringResource(R.string.add_photo),
                            action = {
                                getImageRequest.launch("image/*")
                            },
                            icon = R.drawable.camera
                        )

                        // Agregar botón para video
                        NotesFab(
                            contentDescription = stringResource(R.string.add_video),
                            action = {
                                getVideoRequest.launch("video/*")
                            },
                            icon = R.drawable.video
                        )

                        // Agregar botón para audio
                        NotesFab(
                            contentDescription = stringResource(R.string.add_audio),
                            action = {
                                getAudioRequest.launch("audio/*")
                            },
                            icon = R.drawable.audio
                        )
                    }
                },
                content = {

                    Column(
                        Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (currentPhotos.value != null && currentPhotos.value!!.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = Uri.parse(currentPhotos.value))
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.3f)
                                    .padding(6.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        TextField(
                            value = currentTitle.value,
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                                if (currentTitle.value != note.value.title) {
                                    saveButtonState.value = true
                                } else if (currentNote.value == note.value.note &&
                                    currentTitle.value == note.value.title
                                ) {
                                    saveButtonState.value = false
                                }
                            },
                            label = { Text(text = "Titulo") }
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                        TextField(
                            value = currentNote.value,
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentNote.value = value
                                if (currentNote.value != note.value.note) {
                                    saveButtonState.value = true
                                } else if (currentNote.value == note.value.note &&
                                    currentTitle.value == note.value.title
                                ) {
                                    saveButtonState.value = false
                                }
                            },
                            label = { Text(text = "Contenido") }
                        )
                    }
                }

            )
        }
    }
}
