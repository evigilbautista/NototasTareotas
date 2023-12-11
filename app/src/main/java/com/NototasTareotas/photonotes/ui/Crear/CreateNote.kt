package com.NototasTareotas.photonotes.ui.Crear

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.NototasTareotas.photonotes.PhotoNotesApp
import com.NototasTareotas.photonotes.R

import com.NototasTareotas.photonotes.ui.GenericAppBar
import com.NototasTareotas.photonotes.ui.Lista.NotesFab
import com.NototasTareotas.photonotes.ui.NotesViewModel
import com.NototasTareotas.photonotes.ui.Otros.CameraButtonExample
import com.NototasTareotas.photonotes.ui.theme.PhotoNotesTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel,


    )


{

    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val currentPhotos = remember { mutableStateOf("") }
    val currentVideo = remember { mutableStateOf("") }
    val currentAudio = remember { mutableStateOf("") }
    val currentTipo = remember { mutableStateOf(1) }
    val saveButtonState = remember { mutableStateOf(false) }
    val notif = LocalContext.current
    var tiempoDeEspera = 3000L





    fun modificarTiempoDeEspera(agregar: Boolean, minutos: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = tiempoDeEspera

        if (agregar) {
            calendar.add(Calendar.MINUTE, minutos)
        } else {
            calendar.add(Calendar.MINUTE, -minutos)
        }

        tiempoDeEspera = calendar.timeInMillis
    }

    fun notifica() {
        CrearcanalNotificacion("canal_notificacion", notif)
        notificaciones(
            notif,
            "canal_notificacion",
            1,
            "Nota Creada",
            "Tu nota ha sido creada exitosamente."
        )
    }




    fun botonMasPresionado() {
        modificarTiempoDeEspera(true, 1) // Puedes ajustar la cantidad de minutos que deseas agregar
    }

    // Botón de menos
    fun botonMenosPresionado() {
        modificarTiempoDeEspera(false, 1) // Puedes ajustar la cantidad de minutos que deseas restar
    }

    fun botonMasHorasPresionado() {
        modificarTiempoDeEspera(
            true,
            60
        ) // Puedes ajustar la cantidad de minutos que deseas agregar
    }

    // Botón de menos
    fun botonMenosHorasPresionado() {
        modificarTiempoDeEspera(
            false,
            60
        ) // Puedes ajustar la cantidad de minutos que deseas restar
    }


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
                                currentAudio.value,
                                currentTipo.value,
                            )





                            GlobalScope.launch {
                                delay(tiempoDeEspera)
                                notifica()
                            }


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

                        CameraButtonExample()
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
                        var textoMinutos by remember { mutableStateOf("0") }
                        var contador by remember { mutableStateOf(0) }
                        var textohoras by remember { mutableStateOf("0") }
                        var contadorh by remember { mutableStateOf(0) }


                        Row {
                            TextField(
                                value = textoMinutos,
                                onValueChange = {
                                    textoMinutos = it
                                    // Maneja la entrada de texto si es necesario
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.width(60.dp),
                            )
                            Spacer(modifier = Modifier.padding(18.dp))


                            TextField(
                                value = textohoras,
                                onValueChange = {
                                    textohoras = it
                                    // Maneja la entrada de texto si es necesario
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.width(60.dp),
                            )

                        }

                        Row {
                            Column {


                                Button(onClick = {
                                    if (contador <= 60) { // Verifica que el contador no sea menor que 0
                                        botonMasPresionado()
                                        contador += 1
                                        textoMinutos = contador.toString()
                                    }
                                }
                                ) {
                                    Text("+ minuto")
                                }

                                Spacer(modifier = Modifier.width(18.dp))

                                Button(onClick = {
                                    if (contador > 0) { // Verifica que el contador no sea menor que 0
                                        botonMenosPresionado()
                                        contador -= 1
                                        textoMinutos = contador.toString()
                                    }
                                }) {
                                    Text("- minuto")
                                }
                            }

                            Column {
                                Spacer(modifier = Modifier.width(25.dp))
                                Button(onClick = {

                                    if (contadorh <= 24) {
                                        botonMasHorasPresionado()
                                        contadorh += 1
                                        textohoras = contadorh.toString()
                                    }
                                }
                                ) {
                                    Text("+ horas")
                                }

                                Spacer(modifier = Modifier.width(25.dp))

                                Button(onClick = {
                                    if (contadorh > 0) {
                                        botonMenosHorasPresionado()
                                        contadorh -= 1
                                        textohoras = contadorh.toString()
                                    }
                                }) {
                                    Text("- horas")
                                }
                            }
                        }

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


// TAREAS

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateHWScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val currentPhotos = remember { mutableStateOf("") }
    val currentVideo = remember { mutableStateOf("") }
    val currentAudio = remember { mutableStateOf("") }
    val currentTipo = remember { mutableStateOf(2) }
    val saveButtonState = remember { mutableStateOf(false) }
    val notif = LocalContext.current


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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.secondary
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Crear Tarea",
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
                                currentAudio.value,
                                currentTipo.value,
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
                        CameraButtonExample()

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

                        var isMapDialogVisible by remember {
                            mutableStateOf(
                                false
                            )
                        }

                        FloatingActionButton(
                            onClick = {
                                isMapDialogVisible = true
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = stringResource(R.string.add_ubicacion),
                                    tint = Color.Black
                                )
                            }
                        )

                        if (isMapDialogVisible) {
                            AlertDialog(
                                onDismissRequest = {
                                    isMapDialogVisible = false
                                },
                                text = {

                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            isMapDialogVisible = false
                                        }
                                    ) {
                                        Text("Cerrar")
                                    }
                                }
                            )
                        }
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
                        Spacer(modifier = Modifier.padding(12.dp))
                        // Seccion de recordatorio
                    }
                }
            )
        }
    }
}





fun notificaciones( context: Context,
                    idCanal: String,
                    idNotificacion: Int,
                    titulo: String,
                    texto: String,
                    priority: Int = NotificationCompat.PRIORITY_DEFAULT) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val builder = NotificationCompat.Builder(context, idCanal)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(priority)
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(idNotificacion, builder.build())
        }
    }
}


fun CrearcanalNotificacion(idCanal: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val nombre = "Terminaste los deberes?"
        val descripcion = "ya los terminaste"
        val importancia = NotificationManager.IMPORTANCE_HIGH
        val canal = NotificationChannel(idCanal, nombre, importancia)
            .apply { description = descripcion }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(canal)
    }
}



