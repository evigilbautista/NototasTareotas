package com.NototasTareotas.photonotes

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.NototasTareotas.photonotes.R
import com.NototasTareotas.photonotes.ui.GenericAppBar
import com.NototasTareotas.photonotes.ui.NotesViewModel
import com.NototasTareotas.photonotes.ui.createNote.VideoPlayer
import com.NototasTareotas.photonotes.ui.theme.PhotoNotesTheme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable


import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import com.NototasTareotas.photonotes.PhotoNotesApp
import com.NototasTareotas.photonotes.ui.NotesList.NotesFab


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val currentPhotos = remember { mutableStateOf("") }
    val currentVideo = remember { mutableStateOf("") }
    val currentAudio = remember { mutableStateOf("") }
    val saveButtonState = remember { mutableStateOf(false) }

    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        if (it != null) {
            PhotoNotesApp.getUriPermission(it)
        }
        currentPhotos.value = it.toString()
    }

    val getVideoRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        if (it != null) {
            PhotoNotesApp.getUriPermission(it)
        }
        currentVideo.value = it.toString()
    }

    val getAudioRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        if (it != null) {
            PhotoNotesApp.getUriPermission(it)
        }
        currentAudio.value = it.toString()
    }

    PhotoNotesTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Crear Nota ",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.save_note),
                                tint = Color.Black,
                            )
                        },
                        onIconClick = {
                            viewModel.createNote(
                                currentTitle.value,
                                currentNote.value,
                                currentPhotos.value,
                                currentVideo.value,
                                currentAudio.value
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
                        NotesFab(
                            contentDescription = stringResource(R.string.add_image),
                            action = {
                                getImageRequest.launch(arrayOf("image/*"))
                            },
                            icon = R.drawable.camera
                        )

                        NotesFab(
                            contentDescription = stringResource(R.string.add_audio),
                            action = {
                                getAudioRequest.launch(arrayOf("audio/*"))
                            },
                            icon = R.drawable.audio
                        )

                        NotesFab(
                            contentDescription = stringResource(R.string.add_video),
                            action = {
                                getVideoRequest.launch(arrayOf("video/*"))
                            },
                            icon = R.drawable.video
                        )
                    }
                },
                content = {
                    Column(
                        Modifier
                            .padding(12.dp)
                            .fillMaxSize()

                    ) {
                        if (currentPhotos.value.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = Uri.parse(currentPhotos.value))
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight(0.3f)
                                    .padding(6.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (currentAudio.value.isNotEmpty()) {
                            AudioPlayer(audioUrl = currentAudio.value)
                        }

                        if (currentVideo.value.isNotEmpty()) {
                            VideoPlayer(videoUrl = currentVideo.value)
                        }

                        Spacer(modifier = Modifier.padding(12.dp))

                        TextField(
                            value = currentTitle.value,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                                saveButtonState.value =
                                    currentTitle.value != "" && currentNote.value != ""
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
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .fillMaxWidth(),
                            onValueChange = { value ->
                                currentNote.value = value
                                saveButtonState.value =
                                    currentTitle.value != "" && currentNote.value != ""
                            },
                            label = { Text(text = "Contenido") }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val videoView = remember {
        VideoView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    DisposableEffect(videoUrl) {
        try {
            val mediaController = MediaController(videoView.context)
            mediaController.setAnchorView(videoView)

            videoView.setMediaController(mediaController)
            videoView.setVideoPath(videoUrl)
            videoView.requestFocus()
            videoView.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            videoView.stopPlayback()
        }
    }

    AndroidView(
        factory = { videoView },
        modifier = Modifier
            .fillMaxHeight(0.3f)
            .padding(6.dp)
    )
}

@Composable
fun AudioPlayer(audioUrl: String) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    val isPlaying = remember { mutableStateOf(true) }

    DisposableEffect(audioUrl) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context, Uri.parse(audioUrl))
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            // Manejar errores al reproducir el audio
            e.printStackTrace()
        }

        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(
            text = "Reproduciendo: ${audioUrl.substringAfterLast('/')}",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Gray)
                .padding(8.dp)
        )

        IconButton(
            onClick = {
                if (isPlaying.value) {
                    mediaPlayer.pause()
                } else {
                    mediaPlayer.start()
                }
                isPlaying.value = !isPlaying.value
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = if (isPlaying.value) Icons.Default.Refresh else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}