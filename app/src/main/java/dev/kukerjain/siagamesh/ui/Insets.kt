package dev.kukerjain.siagamesh.ui

import android.os.Build
import android.view.View
import android.view.WindowInsets

/**
 * targetSdk 35+ memaksa edge-to-edge. Tambahkan inset system bar (dan keyboard)
 * ke padding awal view supaya konten tidak tertutup status/navigation bar.
 */
fun View.applySystemBarPadding() {
    val start = intArrayOf(paddingLeft, paddingTop, paddingRight, paddingBottom)
    setOnApplyWindowInsetsListener { v, insets ->
        val l: Int
        val t: Int
        val r: Int
        val b: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val i = insets.getInsets(
                WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout() or WindowInsets.Type.ime(),
            )
            l = i.left; t = i.top; r = i.right; b = i.bottom
        } else {
            @Suppress("DEPRECATION")
            run {
                l = insets.systemWindowInsetLeft; t = insets.systemWindowInsetTop
                r = insets.systemWindowInsetRight; b = insets.systemWindowInsetBottom
            }
        }
        v.setPadding(start[0] + l, start[1] + t, start[2] + r, start[3] + b)
        insets
    }
    requestApplyInsets()
}
