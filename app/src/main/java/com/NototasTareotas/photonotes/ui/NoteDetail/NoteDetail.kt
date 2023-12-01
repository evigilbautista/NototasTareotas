package com.NototasTareotas.photonotes.ui.NoteDetail

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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.NototasTareotas.photonotes.Constants
import com.NototasTareotas.photonotes.Constants.noteDetailPlaceHolder
import com.NototasTareotas.photonotes.PhotoNotesApp
import com.NototasTareotas.photonotes.ui.NotesList.NotesFab
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteDetailScreen(noteId: Int, navController: NavController, viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val note = remember {
        mutableStateOf(noteDetailPlaceHolder)
    }

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    val videoUri = note.value.videoUri

    LaunchedEffect(videoUri) {
        if (!videoUri.isNullOrEmpty()) {
            // Prepara el reproductor de video con la URI del video
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: noteDetailPlaceHolder
        }
    }

    PhotoNotesTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = note.value.title,
                        onIconClick = {
                            navController.navigate(Constants.noteEditNavigation(note.value.id ?: 0))
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.edit_note),
                                contentDescription = stringResource(R.string.edit_note),
                                tint = Color.Black,
                            )
                        },
                        iconState = remember { mutableStateOf(true) }
                    )
                },
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                ) {
                    if (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = Uri.parse(note.value.imageUri))
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth()
                                .padding(6.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Reproductor de video
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AndroidView(
                            factory = { context ->
                                PlayerView(context).apply {
                                    player = exoPlayer
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    // Resto de tu contenido
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = note.value.title,
                            modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 24.dp),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = note.value.dateUpdated, Modifier.padding(12.dp), color = Color.Gray)
                        Text(text = note.value.note, Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}
