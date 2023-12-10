package com.NototasTareotas.photonotes.ui.Otros

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.NototasTareotas.photonotes.R
import com.NototasTareotas.photonotes.ui.Otros.Navegacion.idCanal


class alarma : BroadcastReceiver() {
companion object {
    const val NOTIFICATION_ID = 2
}
    override fun onReceive(context: Context, intent: Intent) {
        alarma(context)
    }

    private fun alarma(context: Context) {
        val notificacion = NotificationCompat.Builder(context, idCanal)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Terminaste los deberes?")
            .setContentText("ya los terminaste")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notificacion)
    }
}

