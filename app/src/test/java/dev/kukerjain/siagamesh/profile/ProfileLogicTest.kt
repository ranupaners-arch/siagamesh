package dev.kukerjain.siagamesh.profile

import dev.kukerjain.siagamesh.mesh.MeshPermissions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random

class ProfileLogicTest {

    @Test
    fun `nama dirapikan dari spasi berlebih`() {
        assertEquals(DisplayName.Result.Valid("Bu Sari RT 03"), DisplayName.validate("  Bu   Sari\tRT 03 "))
    }

    @Test
    fun `nama kosong atau hanya spasi ditolak`() {
        assertEquals(DisplayName.Result.Empty, DisplayName.validate(""))
        assertEquals(DisplayName.Result.Empty, DisplayName.validate("   \n "))
    }

    @Test
    fun `batas panjang nama dihitung per code point`() {
        assertTrue(DisplayName.validate("a".repeat(DisplayName.MAX_LENGTH)) is DisplayName.Result.Valid)
        assertEquals(DisplayName.Result.TooLong, DisplayName.validate("a".repeat(DisplayName.MAX_LENGTH + 1)))
        // Emoji = 2 char UTF-16 tapi 1 code point.
        assertTrue(DisplayName.validate("😀".repeat(DisplayName.MAX_LENGTH)) is DisplayName.Result.Valid)
    }

    @Test
    fun `device id berformat 16 hex kecil dan acak`() {
        val a = DeviceId.generate(Random(1))
        val b = DeviceId.generate(Random(2))
        assertTrue(DeviceId.isValid(a))
        assertTrue(a != b)
        assertFalse(DeviceId.isValid(null))
        assertFalse(DeviceId.isValid("ABCDEF0123456789"))
        assertFalse(DeviceId.isValid("abc"))
    }

    @Test
    fun `device id ditampilkan per 4 karakter`() {
        assertEquals("A1B2 C3D4 E5F6 0718", DeviceId.pretty("a1b2c3d4e5f60718"))
    }

    @Test
    fun `izin per versi android`() {
        assertEquals(listOf("android.permission.ACCESS_FINE_LOCATION"), MeshPermissions.requiredFor(24))
        val s = MeshPermissions.requiredFor(31)
        assertTrue("android.permission.BLUETOOTH_SCAN" in s)
        assertTrue("android.permission.ACCESS_FINE_LOCATION" in s)
        val t = MeshPermissions.requiredFor(33)
        assertFalse("android.permission.ACCESS_FINE_LOCATION" in t)
        assertTrue("android.permission.NEARBY_WIFI_DEVICES" in t)
        assertTrue("android.permission.POST_NOTIFICATIONS" in t)
    }
}
