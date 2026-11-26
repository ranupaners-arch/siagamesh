package dev.kukerjain.siagamesh.profile

import android.content.Context
import android.content.SharedPreferences

/** Profil lokal: nama tampilan + ID perangkat acak yang persist. */
class ProfileStore(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** Dibuat sekali, lalu selalu sama selama app tidak di-uninstall / data dihapus. */
    val deviceId: String
        get() {
            val existing = prefs.getString(KEY_DEVICE_ID, null)
            if (DeviceId.isValid(existing)) return existing!!
            val fresh = DeviceId.generate()
            prefs.edit().putString(KEY_DEVICE_ID, fresh).apply()
            return fresh
        }

    val displayName: String?
        get() = prefs.getString(KEY_DISPLAY_NAME, null)?.takeIf { it.isNotBlank() }

    val hasProfile: Boolean
        get() = displayName != null

    /** @return hasil validasi; nama hanya disimpan bila [DisplayName.Result.Valid]. */
    fun saveDisplayName(raw: String): DisplayName.Result {
        val result = DisplayName.validate(raw)
        if (result is DisplayName.Result.Valid) {
            prefs.edit().putString(KEY_DISPLAY_NAME, result.name).apply()
        }
        return result
    }

    private companion object {
        const val PREFS = "profile"
        const val KEY_DEVICE_ID = "device_id"
        const val KEY_DISPLAY_NAME = "display_name"
    }
}
