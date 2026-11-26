package dev.kukerjain.siagamesh.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import dev.kukerjain.siagamesh.R
import dev.kukerjain.siagamesh.mesh.MeshPermissions
import dev.kukerjain.siagamesh.profile.DisplayName
import dev.kukerjain.siagamesh.profile.ProfileStore

/**
 * Onboarding satu layar: jelaskan & minta izin radio, lalu isi nama tampilan.
 * Juga dipakai untuk "Ubah nama" dari home ([EXTRA_EDIT]).
 */
class OnboardingActivity : Activity() {

    private lateinit var profile: ProfileStore
    private lateinit var permissionStatus: TextView
    private lateinit var permissionButton: Button
    private lateinit var nameInput: EditText
    private lateinit var nameError: TextView

    /** Sudah pernah minta izin di sesi ini → kalau masih ditolak permanen, arahkan ke Pengaturan. */
    private var askedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        findViewById<View>(R.id.root).applySystemBarPadding()

        profile = ProfileStore(this)
        permissionStatus = findViewById(R.id.permission_status)
        permissionButton = findViewById(R.id.permission_button)
        nameInput = findViewById(R.id.name_input)
        nameError = findViewById(R.id.name_error)
        askedOnce = savedInstanceState?.getBoolean(STATE_ASKED) ?: false

        val editing = intent.getBooleanExtra(EXTRA_EDIT, false)
        if (editing) {
            findViewById<TextView>(R.id.title).setText(R.string.onboarding_title_edit)
            findViewById<View>(R.id.permission_section).visibility = View.GONE
        }
        profile.displayName?.let {
            nameInput.setText(it)
            nameInput.setSelection(nameInput.text.length)
        }

        permissionButton.setOnClickListener { onPermissionButton() }
        findViewById<Button>(R.id.save_button).setOnClickListener { save() }
        nameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { save(); true } else false
        }
    }

    override fun onResume() {
        super.onResume()
        renderPermissionState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_ASKED, askedOnce)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) renderPermissionState()
    }

    private fun onPermissionButton() {
        val missing = MeshPermissions.missing(this)
        if (missing.isEmpty()) return
        val blocked = askedOnce && missing.none { shouldShowRequestPermissionRationale(it) }
        if (blocked) {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)),
            )
        } else {
            askedOnce = true
            requestPermissions(missing.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    private fun renderPermissionState() {
        val missing = MeshPermissions.missing(this)
        when {
            missing.isEmpty() -> {
                permissionStatus.setText(R.string.permission_status_ok)
                permissionStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                permissionButton.visibility = View.GONE
            }
            else -> {
                permissionStatus.setText(R.string.permission_status_missing)
                permissionStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0)
                permissionButton.visibility = View.VISIBLE
                val blocked = askedOnce && missing.none { shouldShowRequestPermissionRationale(it) }
                permissionButton.setText(
                    if (blocked) R.string.permission_open_settings else R.string.permission_grant,
                )
            }
        }
    }

    private fun save() {
        when (profile.saveDisplayName(nameInput.text.toString())) {
            is DisplayName.Result.Valid -> {
                nameError.visibility = View.GONE
                if (!intent.getBooleanExtra(EXTRA_EDIT, false)) {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    )
                }
                finish()
            }
            DisplayName.Result.Empty -> showNameError(getString(R.string.name_error_empty))
            DisplayName.Result.TooLong -> showNameError(
                getString(R.string.name_error_too_long, DisplayName.MAX_LENGTH),
            )
        }
    }

    private fun showNameError(message: String) {
        nameError.text = message
        nameError.visibility = View.VISIBLE
        nameInput.requestFocus()
    }

    companion object {
        private const val REQUEST_PERMISSIONS = 10
        private const val STATE_ASKED = "asked_once"
        private const val EXTRA_EDIT = "edit"

        fun editIntent(context: Context): Intent =
            Intent(context, OnboardingActivity::class.java).putExtra(EXTRA_EDIT, true)
    }
}
