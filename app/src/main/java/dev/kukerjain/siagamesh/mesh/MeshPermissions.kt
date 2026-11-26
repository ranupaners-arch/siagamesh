package dev.kukerjain.siagamesh.mesh

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/** Izin runtime yang dibutuhkan mesh, berbeda per versi Android. */
object MeshPermissions {
    private const val FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"
    private const val BLUETOOTH_SCAN = "android.permission.BLUETOOTH_SCAN"
    private const val BLUETOOTH_ADVERTISE = "android.permission.BLUETOOTH_ADVERTISE"
    private const val BLUETOOTH_CONNECT = "android.permission.BLUETOOTH_CONNECT"
    private const val NEARBY_WIFI_DEVICES = "android.permission.NEARBY_WIFI_DEVICES"
    private const val POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"

    /** Murni terhadap [sdkInt] supaya bisa dites di JVM. */
    fun requiredFor(sdkInt: Int): List<String> = buildList {
        if (sdkInt >= 31) {
            add(BLUETOOTH_SCAN)
            add(BLUETOOTH_ADVERTISE)
            add(BLUETOOTH_CONNECT)
        }
        // ≤ 30: lokasi syarat scan BLE. 31–32: masih syarat Wi-Fi Direct.
        if (sdkInt <= 32) add(FINE_LOCATION)
        if (sdkInt >= 33) {
            add(NEARBY_WIFI_DEVICES)
            add(POST_NOTIFICATIONS)
        }
    }

    fun required(): List<String> = requiredFor(Build.VERSION.SDK_INT)

    fun missing(context: Context): List<String> = required().filter {
        context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
    }

    fun allGranted(context: Context): Boolean = missing(context).isEmpty()
}
