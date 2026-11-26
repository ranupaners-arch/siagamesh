package dev.kukerjain.siagamesh.mesh

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.util.Log
import dev.kukerjain.siagamesh.R
import dev.kukerjain.siagamesh.SiagaMeshApp
import dev.kukerjain.siagamesh.ui.MainActivity

/**
 * Foreground service yang menjaga mesh tetap hidup saat layar mati / app di background.
 * Fase 0: hanya notifikasi persistent. Transport BLE & Wi-Fi Direct masuk di fase 1 dan 4.
 */
class MeshService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopMesh()
            return START_NOT_STICKY
        }
        return try {
            goForeground()
            running = true
            START_STICKY
        } catch (e: RuntimeException) {
            // Mis. ForegroundServiceStartNotAllowedException (Android 12+) bila dimulai dari background.
            Log.w(TAG, "Gagal masuk foreground", e)
            stopMesh()
            START_NOT_STICKY
        }
    }

    override fun onDestroy() {
        running = false
        super.onDestroy()
    }

    private fun goForeground() {
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE,
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun stopMesh() {
        running = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(): Notification {
        val openApp = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val stop = PendingIntent.getService(
            this, 1,
            Intent(this, MeshService::class.java).setAction(ACTION_STOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, SiagaMeshApp.CHANNEL_MESH_STATUS)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this).setPriority(Notification.PRIORITY_LOW)
        }
        return builder
            .setSmallIcon(R.drawable.ic_stat_mesh)
            .setContentTitle(getString(R.string.notif_mesh_title))
            .setContentText(getString(R.string.notif_mesh_text))
            .setContentIntent(openApp)
            .setOngoing(true)
            .setShowWhen(false)
            .setCategory(Notification.CATEGORY_SERVICE)
            .addAction(
                Notification.Action.Builder(null as Icon?, getString(R.string.notif_mesh_stop), stop).build(),
            )
            .build()
    }

    companion object {
        private const val TAG = "MeshService"
        private const val NOTIFICATION_ID = 1001
        private const val ACTION_STOP = "dev.kukerjain.siagamesh.action.STOP_MESH"

        /** Status kasar untuk UI. Cukup untuk fase 0; nanti diganti state mesh sungguhan. */
        @Volatile
        var running: Boolean = false
            private set

        fun start(context: Context) {
            val intent = Intent(context, MeshService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.startService(Intent(context, MeshService::class.java).setAction(ACTION_STOP))
        }
    }
}
