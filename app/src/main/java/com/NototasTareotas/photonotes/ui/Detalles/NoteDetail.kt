package com.NototasTareotas.photonotes.ui.Detalles


import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.NototasTareotas.photonotes.ui.Otros.Navegacion
import com.NototasTareotas.photonotes.ui.Otros.Navegacion.noteDetailPlaceHolder
import com.NototasTareotas.photonotes.R
import com.NototasTareotas.photonotes.ui.GenericAppBar
import com.NototasTareotas.photonotes.ui.NotesViewModel
import com.NototasTareotas.photonotes.ui.theme.PhotoNotesTheme
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

    val videoExoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    val audioExoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    val audioUri = note.value.audioUri
    val videoUri = note.value.videoUri

    LaunchedEffect(audioUri) {
        if (!audioUri.isNullOrEmpty()) {
            // Prepara el reproductor de audio con la URI del audio
            val audioMediaItem = MediaItem.fromUri(audioUri)
            audioExoPlayer.setMediaItem(audioMediaItem)
            audioExoPlayer.prepare()
            audioExoPlayer.play()
        }
    }

    LaunchedEffect(videoUri) {
        if (!videoUri.isNullOrEmpty()) {
            // Prepara el reproductor de video con la URI del video
            val videoMediaItem = MediaItem.fromUri(videoUri)
            videoExoPlayer.setMediaItem(videoMediaItem)
            videoExoPlayer.prepare()
            videoExoPlayer.play()
        }
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: noteDetailPlaceHolder
        }
    }

    PhotoNotesTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Editar nota",
                        onIconClick = {
                            navController.navigate(Navegacion.noteEditNavigation(note.value.id ?: 0))
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
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = note.value.title,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(text = note.value.dateUpdated, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                            Text(text = note.value.note)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(12.dp))
                    }


                    // Video e imagen en la misma fila
                    if ((videoUri != null && videoUri.isNotEmpty()) || (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty())) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                //Video
                                if (videoUri != null && videoUri.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(200.dp)
                                    ) {
                                        AndroidView(
                                            factory = { context ->
                                                PlayerView(context).apply {
                                                    player = videoExoPlayer
                                                    layoutParams = ViewGroup.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.MATCH_PARENT
                                                    )
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                }

                                // Imagen
                                if (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(200.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                ImageRequest
                                                    .Builder(LocalContext.current)
                                                    .data(data = Uri.parse(note.value.imageUri))
                                                    .build()
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(6.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(12.dp))
                    }

                    // Sección de audio
                    if (audioUri != null && audioUri.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        PlayerView(context).apply {
                                            player = audioExoPlayer
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HWDetailScreen(noteId: Int, navController: NavController, viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val note = remember {
        mutableStateOf(noteDetailPlaceHolder)
    }

    val videoExoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    val audioExoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    val audioUri = note.value.audioUri
    val videoUri = note.value.videoUri

    LaunchedEffect(audioUri) {
        if (!audioUri.isNullOrEmpty()) {
            // Prepara el reproductor de audio con la URI del audio
            val audioMediaItem = MediaItem.fromUri(audioUri)
            audioExoPlayer.setMediaItem(audioMediaItem)
            audioExoPlayer.prepare()
            audioExoPlayer.play()
        }
    }

    LaunchedEffect(videoUri) {
        if (!videoUri.isNullOrEmpty()) {
            // Prepara el reproductor de video con la URI del video
            val videoMediaItem = MediaItem.fromUri(videoUri)
            videoExoPlayer.setMediaItem(videoMediaItem)
            videoExoPlayer.prepare()
            videoExoPlayer.play()
        }
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: noteDetailPlaceHolder
        }
    }

    PhotoNotesTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Editar tarea",
                        onIconClick = {
                            navController.navigate(Navegacion.HWEditNavigation(note.value.id ?: 0))
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
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = note.value.title,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(text = note.value.dateUpdated, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                            Text(text = note.value.note)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(12.dp))
                    }


                    // Video e imagen en la misma fila
                    if ((videoUri != null && videoUri.isNotEmpty()) || (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty())) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                //Video
                                if (videoUri != null && videoUri.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(200.dp)
                                    ) {
                                        AndroidView(
                                            factory = { context ->
                                                PlayerView(context).apply {
                                                    player = videoExoPlayer
                                                    layoutParams = ViewGroup.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.MATCH_PARENT
                                                    )
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                }

                                // Imagen
                                if (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(200.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                ImageRequest
                                                    .Builder(LocalContext.current)
                                                    .data(data = Uri.parse(note.value.imageUri))
                                                    .build()
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(6.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(12.dp))
                    }

                    // Sección de audio
                    if (audioUri != null && audioUri.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        PlayerView(context).apply {
                                            player = audioExoPlayer
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
