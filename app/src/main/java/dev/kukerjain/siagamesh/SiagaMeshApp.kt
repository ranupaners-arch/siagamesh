package dev.kukerjain.siagamesh

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class SiagaMeshApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        val meshStatus = NotificationChannel(
            CHANNEL_MESH_STATUS,
            getString(R.string.channel_mesh_status),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = getString(R.string.channel_mesh_status_desc)
            setShowBadge(false)
        }
        manager.createNotificationChannel(meshStatus)
    }

    companion object {
        const val CHANNEL_MESH_STATUS = "mesh_status"
    }
}
