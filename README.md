# SiagaMesh

Aplikasi mesh offline Android (BLE + Wi-Fi Direct) untuk situasi bencana, RT, dan keluarga. Menyambungkan HP ke HP tanpa BTS, pulsa, atau internet: chat grup, tombol SOS, dan absen sekali tekan.

Arah produk dekat Covatom, tetapi UI dan kode dibuat sendiri — bukan salinan merek atau asetnya.

**Status:** Tahap dokumentasi — kode belum dimulai.

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
