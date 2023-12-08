package com.NototasTareotas.photonotes.ui.createNote

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
//import androidx.compose.ui.graphics.painter.asPainter

@Preview
@Composable
fun CameraButtonExample() {
    val context = LocalContext.current
    // Estado para almacenar la imagen capturada
    var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Manejo del bitmap de la imagen capturada
        if (bitmap != null) {
            capturedImage = bitmap.asImageBitmap()
        }
    }

    Column {
        Button(onClick = {
            if (hasCameraPermission(context)) {
                launcher.launch(null)
            } else {
                // Solicitar permiso
                requestCameraPermission(context)
            }
        }) {
            Text("Abrir Cámara")
        }




            // Botón para guardar la imagen en la galería
            Button(onClick = {
                capturedImage?.let { captured ->
                    saveImageToGallery(context, captured)
                }
            }) {
                Text("Guardar en Galería")
            }
        }
    }


fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestCameraPermission(activity: Context) {
    ActivityCompat.requestPermissions(
        activity as Activity,
        arrayOf(Manifest.permission.CAMERA),
        PERMISSION_CAMERA_CODE
    )
}

const val PERMISSION_CAMERA_CODE = 101





fun saveImageToGallery(context: Context, image: ImageBitmap) {
    // Crear un nombre de archivo único
    val fileName = "captured_image_${System.currentTimeMillis()}.png"

    // Obtener el directorio de imágenes públicas
    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    // Crear el archivo en el directorio de imágenes
    val file = File(imagesDir, fileName)

    // Obtener el OutputStream del archivo
    val outputStream: OutputStream = FileOutputStream(file)

    // Convertir ImageBitmap a Bitmap y guardar en el OutputStream
    val bitmap = image.asAndroidBitmap() // Si asImageBitmap() ya ha sido llamado
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

    // Cerrar el OutputStream
    outputStream.close()

    // Notificar a la Galería de la nueva imagen para que aparezca en la galería de fotos
    MediaScannerConnection.scanFile(
        context,
        arrayOf(file.absolutePath),
        arrayOf("image/png"),
        null
    )

    Toast.makeText(context, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show()
}
