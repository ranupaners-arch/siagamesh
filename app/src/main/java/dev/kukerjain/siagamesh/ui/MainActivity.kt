package dev.kukerjain.siagamesh.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import dev.kukerjain.siagamesh.BuildConfig
import dev.kukerjain.siagamesh.R
import dev.kukerjain.siagamesh.mesh.MeshPermissions
import dev.kukerjain.siagamesh.mesh.MeshService
import dev.kukerjain.siagamesh.profile.DeviceId
import dev.kukerjain.siagamesh.profile.ProfileStore

/**
 * Home fase 0: profil, status mesh, dan peringatan izin.
 * Chat grup, SOS, dan absen menyusul di fase 2–3.
 */
class MainActivity : Activity() {

    private lateinit var profile: ProfileStore
    private lateinit var meshStatus: TextView
    private lateinit var meshToggle: Button
    private lateinit var permissionWarning: View
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profile = ProfileStore(this)
        if (!profile.hasProfile) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.root).applySystemBarPadding()
        meshStatus = findViewById(R.id.mesh_status)
        meshToggle = findViewById(R.id.mesh_toggle)
        permissionWarning = findViewById(R.id.permission_warning)

        findViewById<TextView>(R.id.device_id).text = DeviceId.pretty(profile.deviceId)
        findViewById<TextView>(R.id.version).text = getString(R.string.version_label, BuildConfig.VERSION_NAME)
        findViewById<Button>(R.id.edit_name).setOnClickListener {
            startActivity(OnboardingActivity.editIntent(this))
        }
        findViewById<Button>(R.id.permission_fix).setOnClickListener {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        meshToggle.setOnClickListener {
            if (MeshService.running) MeshService.stop(this) else MeshService.start(this)
            // Service berjalan async; render ulang sebentar lagi.
            handler.postDelayed(::render, 300)
        }

        // Mesh hidup otomatis saat app dibuka (activity di foreground → boleh start FGS).
        if (savedInstanceState == null) MeshService.start(this)
    }

    override fun onResume() {
        super.onResume()
        if (::meshStatus.isInitialized) {
            findViewById<TextView>(R.id.display_name).text = profile.displayName
            render()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun render() {
        val on = MeshService.running
        meshStatus.setText(if (on) R.string.mesh_on else R.string.mesh_off)
        meshStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(
            if (on) R.drawable.ic_check else R.drawable.ic_off, 0, 0, 0,
        )
        meshToggle.setText(if (on) R.string.mesh_stop else R.string.mesh_start)
        permissionWarning.visibility =
            if (MeshPermissions.allGranted(this)) View.GONE else View.VISIBLE
    }
}
