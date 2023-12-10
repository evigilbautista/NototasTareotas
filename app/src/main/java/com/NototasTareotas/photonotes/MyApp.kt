package com.NototasTareotas.photonotes

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.NototasTareotas.photonotes.ui.Otros.alarma
import com.NototasTareotas.photonotes.ui.Otros.alarma.Companion.NOTIFICATION_ID

@Composable
fun notifica(){
    val context = LocalContext.current
    val idCanal = "Terminaste los deberes"
    val idNotifcacion = 0




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

fun notificacionProgramada(context: Context) {
    val intent = Intent(context, alarma::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent)

}