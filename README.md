# SiagaMesh

Aplikasi mesh offline Android (BLE + Wi-Fi Direct) untuk situasi bencana, RT, dan keluarga. Menyambungkan HP ke HP tanpa BTS, pulsa, atau internet: chat grup, tombol SOS, dan absen sekali tekan.

Arah produk dekat Covatom, tetapi UI dan kode dibuat sendiri — bukan salinan merek atau asetnya.

**Status:** Fase 0 (skeleton) — profil lokal, izin radio, dan foreground service mesh. Transport belum ada.

## Build

Butuh JDK 17+ dan Android SDK (platform 36).

```bash
./gradlew assembleDebug        # app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest    # tes unit logika profil & izin
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Stack

- Native Kotlin + Android View biasa (bukan Jetpack Compose, bukan Flutter)
- minSdk 24 (Android 7+)
- Package: `dev.kukerjain.siagamesh`
- Target APK di bawah 3 MB, ringan di HP RAM 2 GB
- Transport: BLE mesh + Wi-Fi Direct

## Fitur per fase

### MVP

- Chat grup
- Buat / join grup via QR
- SOS
- Absen sekali tekan
- Daftar anggota + radar

### V2

- Pengumuman
- DM terenkripsi (prompt Percayai)
- Mode jembatan / relay
- SOS tombol volume 5x
- Senter SOS
- Titik kumpul + kompas
- Kartu medis
- Kontak SMS darurat
- Hemat baterai

### V3

- Walkie-talkie push-to-talk
- Voice note
- Kirim foto / video / file via Wi-Fi Direct
- Peta offline
- Cari korban / keluarga
- Tag Bluetooth iTag (~30 m; AirTag/SmartTag tidak didukung)
- Panduan P3K offline

### V4

- Web posko via Wi-Fi lokal
- LoRa / Meshtastic

## Dokumentasi

Spesifikasi produk lengkap ada di [docs/PRD.md](docs/PRD.md).
