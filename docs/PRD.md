# PRD — SiagaMesh (mesh offline bencana / RT)

Spesifikasi produk SiagaMesh: aplikasi Android native Kotlin yang ringan untuk HP murah, yang menyambungkan HP ke HP tanpa internet: chat grup, SOS, dan absen lewat BLE + Wi-Fi Direct.

Dokumen ini merangkum keputusan produk 23 November 2026 dan pemetaan fitur dari analisis APK Covatom v0.61.0. Arah produk dekat Covatom, tetapi UI dan kode dibuat sendiri — bukan salinan merek atau asetnya.

**Status dokumen:** Tahap dokumentasi — kode belum dimulai.

## Daftar isi

- [Ringkasan](#ringkasan)
- [Nama](#nama)
- [Daftar fitur](#daftar-fitur)
- [Masalah & tujuan](#masalah--tujuan)
- [Pengguna & skenario](#pengguna--skenario)
- [Ruang lingkup MVP](#ruang-lingkup-mvp)
- [Bukan di MVP](#bukan-di-mvp)
- [Arsitektur teknis](#arsitektur-teknis)
- [Protokol & keamanan](#protokol--keamanan)
- [UI / alur utama](#ui--alur-utama)
- [Prinsip UI & aksesibilitas](#prinsip-ui--aksesibilitas)
- [Kriteria sukses](#kriteria-sukses)
- [Fase rilis](#fase-rilis)
- [Repo & aturan kerja](#repo--aturan-kerja)
- [Risiko](#risiko)

## Ringkasan

Aplikasi Android yang membuat jaringan darurat antar HP di sekitar tanpa BTS, tanpa pulsa, tanpa internet — chat grup, tombol SOS, dan absen sekali tekan.

| | |
| --- | --- |
| Platform v1 | Android 7+ (native Kotlin + Android View, tanpa Flutter/Compose) |
| Target APK | Di bawah 3 MB, jalan lancar di HP RAM 2 GB |
| Transport | BLE mesh + Wi-Fi Direct |
| MVP fitur | Chat grup, buat/join via QR, SOS, absen, daftar anggota + radar |
| Jangkauan demo | Dekat (satu ruang) dan jauh (hop / Wi-Fi Direct ~50–100 m) |
| Repo | Public `ranupaners-arch/siagamesh`, tanpa Co-authored-by |
| Status | Draft PRD — belum coding |

Pengguna memakai app ini dalam stres, cahaya jelek, dan sering di HP entry-level. Jadi UI harus kontras tinggi, tombol besar, status SOS/absen/offline kebaca dalam satu lihat, dan animasi seminimal mungkin.

## Nama

| | |
| --- | --- |
| Nama app | SiagaMesh |
| Package | `dev.kukerjain.siagamesh` |
| Repo | `ranupaners-arch/siagamesh` (public) |
| Diputuskan | 23 November 2026 oleh Ranupane |

## Daftar fitur

Semua fitur yang ada di Covatom v0.61.0 (hasil bongkar APK), dipetakan ke fase SiagaMesh.

| Fitur | Fungsi | Fase |
| --- | --- | --- |
| Chat grup | Pesan teks ke semua anggota grup | MVP |
| Buat / join grup via QR | Undang anggota dengan scan QR | MVP |
| SOS | Alarm darurat ke semua HP sekitar + lokasi | MVP |
| Absen sekali tekan | Ketua cek siapa aman, terluka, belum jawab | MVP |
| Daftar anggota + radar | Siapa yang terdengar di sekitar | MVP |
| Pengumuman | Pesan penting dari ketua, ditandai jika terlewat | V2 |
| Pesan pribadi (DM) | Chat 1-on-1 terenkripsi, ada prompt Percayai | V2 |
| Mode jembatan | HP jadi relay ke grup lain di luar jangkauan | V2 |
| SOS tombol volume | Tekan volume 5x untuk SOS tanpa buka app | V2 |
| Senter SOS | Kedip senter pola SOS | V2 |
| Titik kumpul + kompas | Tentukan titik kumpul, semua dipandu arah | V2 |
| Kartu medis | Golongan darah, alergi, obat — terlihat saat SOS | V2 |
| Kontak SMS darurat | Kirim SMS SOS kalau sinyal muncul sebentar | V2 |
| Hemat baterai | Mode radio jarang scan saat baterai tipis | V2 |
| Walkie-talkie | Push-to-talk suara antar HP | V3 |
| Voice note | Rekam suara singkat | V3 |
| Kirim foto / video / file | Lewat Wi-Fi Direct, ukuran dibatasi | V3 |
| Peta offline | Unduh peta wilayah sebelum bencana | V3 |
| Cari korban / keluarga | Cari HP orang tertentu di sekitar | V3 |
| Tag Bluetooth (iTag) | Pantau anak/lansia lewat gantungan murah ~30 m; AirTag/SmartTag tidak didukung | V3 |
| Panduan offline | P3K dan langkah darurat tanpa internet | V3 |
| Web posko | Laptop posko lihat grup lewat Wi-Fi lokal | V4 |
| LoRa / Meshtastic | Jangkauan beberapa km via alat radio | V4 |

### Catatan UI per fitur

| Fase | Fitur | Layar utama | Catatan UI |
| --- | --- | --- | --- |
| MVP | Chat grup | Daftar percakapan + ruang chat | Gelembung sederhana, status kirim/terima/offline kebaca tanpa warna saja |
| MVP | Buat / join grup via QR | QR masuk + pindai | QR besar, area putih tenang, kode cadangan teks, seluruh tombol ≥48dp |
| MVP | SOS | Tombol SOS + status siaga | Satu aksi primer sangat jelas; konfirmasi anti-salah tekan; state aktif kontras tinggi |
| MVP | Absen sekali tekan | Absen | Satu tombol besar; status hadir/belum + waktu; seluruh baris daftar bisa ketuk |
| MVP | Daftar anggota + radar | Anggota / radar | Nama, status mesh, indikasi jarak/sinyal; bukan peta berat |
| V2 | Pengumuman | Feed pengumuman | Daftar editorial, kategori jelas, prioritas kebaca |
| V2 | DM terenkripsi (prompt Percayai) | DM + dialog Percayai | Prompt keputusan jujur, bukan modal dekoratif |
| V2 | Mode jembatan / relay | Status koneksi / pengaturan radio | Indikator peran (node / jembatan) teks + ikon |
| V2 | SOS tombol volume 5x | Pemicu hardware + konfirmasi | Lebih ke umpan balik haptik/suara/layar kunci daripada ornamen |
| V2 | Senter SOS | Kontrol senter di mode SOS | Toggle besar, state nyala sangat jelas |
| V2 | Titik kumpul + kompas | Arah ke titik kumpul | Kompas sederhana, angka jarak, kontras tinggi; hindari 3D |
| V2 | Kartu medis | Kartu medis offline | Hierarki data seperti slip/tiket: yang krusial paling besar |
| V2 | Kontak SMS darurat | Pengaturan kontak + kirim darurat | Daftar singkat, aksi primer satu |
| V2 | Hemat baterai | Pengaturan + indikator daya | Mode hemat terlihat; jangan andalkan blur/animasi latar |
| V3 | Walkie-talkie push-to-talk | PTT | Tombol tekan-tahan sangat besar; state bicara/mendengar jelas |
| V3 | Voice note | Chat (rekam/putar) | Kontrol rekam sederhana, tanpa waveform berat |
| V3 | Kirim foto / video / file via Wi-Fi Direct | Lampir + status transfer | Progress transfer teks + bar; jangan preview video berat di daftar |
| V3 | Peta offline | Peta | Peta ringan, label besar |
| V3 | Cari korban / keluarga | Pencarian + hasil | Daftar hasil + status terakhir terlihat; bukan heatmap |
| V3 | Tag Bluetooth iTag (~30 m) | Daftar tag + sinyal | Jarak kasar / kekuatan sinyal, bukan peta presisi |
| V3 | Panduan P3K offline | Panduan kategori | Artikel/langkah offline, kategori jelas, tipografi kebaca |
| V4 | Web posko via Wi-Fi lokal | Tampilan posko (web lokal) | UI sangat sederhana di browser HP; bukan dashboard SaaS |
| V4 | LoRa / Meshtastic | Status radio LoRa | Indikator perangkat/sinyal |

## Masalah & tujuan

Saat gempa, banjir, atau listrik padam, jaringan seluler sering mati. Warga di satu RT atau keluarga di satu rumah masih butuh: tahu siapa aman, kirim pesan singkat, dan tekan SOS. Radio walkie dan HT tidak semua orang punya; HP hampir semua orang punya.

**Tujuan produk**

- Komunikasi lokal tanpa internet/pulsa/BTS
- Satu ketua grup bisa absen semua anggota
- SOS terdengar ke HP sekitar yang memasang app

**Bukan tujuan v1**

- Mengganti WhatsApp saat online
- Jaringan nasional / internet fallback
- Hardware LoRa sendiri (Meshtastic belakangan)

## Pengguna & skenario

| Pengguna | Peran | Keterangan |
| --- | --- | --- |
| Ketua RT / koordinator | Primary | Buat grup, undang lewat QR, tekan absen, lihat siapa belum jawab |
| Anggota keluarga / warga | Primary | Join grup, chat, jawab absen, tekan SOS jika butuh bantuan |
| Relawan lapangan | Later | Koordinasi antar pos — di luar MVP kecuali hop BLE sudah stabil |

### Skenario sukses demo

1. **Dekat** — 2–3 HP dalam satu ruangan: buat grup, chat bolak-balik di bawah 2 detik, SOS muncul di semua.
2. **Jauh** — 2 HP jarak ~50–100 m (halaman / antar rumah): Wi-Fi Direct membawa chat; hop BLE lewat HP ketiga di tengah tetap sampai.

## Ruang lingkup MVP

- Profil lokal: nama tampilan + ID perangkat acak (persist)
- Buat / join grup lewat QR atau kode pendek
- Chat grup teks (offline, antrian kirim ulang jika hop gagal)
- Daftar anggota + status last-seen lokal
- SOS: broadcast prioritas tinggi + notifikasi fullscreen / suara
- Absen sekali tekan: ketua kirim poll; anggota jawab aman / butuh bantuan / belum jawab
- Transport BLE: advertise + scan + GATT untuk pesan kecil
- Transport Wi-Fi Direct: grup pemilik sebagai host untuk throughput lebih besar
- Foreground service + notifikasi persistent biar mesh tetap hidup
- Bahasa UI: Indonesia (default)

## Bukan di MVP

- Walkie-talkie / voice note
- Kirim foto / file besar
- Peta offline / GPS share
- Meshtastic / LoRa hardware
- iOS
- Web posko / gateway internet
- E2EE penuh antar semua pesan (cukup identity + AES-GCM channel key dulu)
- Akun cloud / login

## Arsitektur teknis

Stack disepakati 23 November 2026: native Kotlin + Android View biasa (bukan Flutter, bukan Compose), minSdk 24, supaya APK kecil, hemat RAM dan baterai, dan jalan di HP murah.

| Lapisan | Pilihan | Catatan |
| --- | --- | --- |
| UI | Android View + XML, Material ringan | Tanpa Compose; tema terang/gelap |
| Bahasa | Kotlin + coroutines | Tanpa framework DI berat |
| Storage lokal | SQLite bawaan / SharedPreferences | Tanpa Room bila tidak perlu |
| BLE | Android BLE API langsung | Advertise + scan + GATT |
| Wi-Fi Direct | WifiP2pManager langsung | Host = ketua grup |
| Crypto | javax.crypto bawaan | AES-GCM + ECDH P-256, tanpa library |
| QR | ZXing core saja | Seperti Covatom, kecil |

**Kelebihan**

- APK target di bawah 3 MB, buka cepat di HP RAM 2 GB
- Akses radio langsung tanpa jembatan, lebih stabil di background
- Hemat baterai — penting saat listrik padam

**Trade-off**

- Android saja; iOS butuh app terpisah nanti
- UI lebih lama dibuat dibanding Flutter
- BLE mesh custom tetap pekerjaan terbesar

## Protokol & keamanan

1. **Identitas perangkat** — Key pair ECDH P-256 saat first launch; public key di QR join; private key tidak pernah keluar perangkat.
2. **Grup** — Ketua buat `groupId` + channel key acak. Join via QR membawa `groupId` + channel key (atau invite signed).
3. **Paket pesan** — Header: type (`chat` / `sos` / `absen` / `ack` / `hello`), `msgId`, ttl/hop, `senderId`, timestamp. Body dienkripsi AES-GCM dengan channel key.
4. **Relay (jauh)** — HP di tengah yang satu grup (atau mode jembatan) meneruskan paket selama ttl > 0 dan `msgId` belum pernah dilihat (dedupe).
5. **Trust** — Peer baru yang namanya bentrok → prompt Percayai / Tolak sebelum aksi sensitif. Chat grup tetap jalan setelah join QR.

> **Batas keamanan MVP.** QR invite = siapa yang scan bisa masuk grup. Cocok RT/keluarga, bukan untuk publik terbuka. Jangan klaim aman seperti Signal di marketing v1.

## UI / alur utama

1. **Onboarding**
   - Izinkan Bluetooth, lokasi (syarat nearby Android), notifikasi
   - Isi nama tampilan
   - Buat grup atau scan QR
2. **Home grup**
   - Daftar chat
   - Tombol SOS merah selalu terlihat
   - Menu Absen (ketua)
   - Anggota + indikator hop/radio
3. **SOS**
   - Tekan tahan 1 detik → konfirmasi → broadcast
   - Penerima: overlay fullscreen + bunyi sampai di-ack
4. **Absen**
   - Ketua kirim
   - Anggota pilih Aman / Butuh bantuan
   - Ringkasan hitungan real-time di layar ketua

## Prinsip UI & aksesibilitas

UI harus kontras tinggi, tombol besar, dan hemat aset — dipakai dalam stres, cahaya jelek, dan di HP entry-level.

- Kontras WCAG 2.1 AA (teks ≥4.5:1, UI ≥3:1)
- Target sentuh ≥48dp (acuan Tryvia: 44–48dp)
- Status tidak pernah hanya warna: pakai ikon + teks + warna
- Satu set ikon vektor; tanpa raster/Lottie dekoratif
- Hormati pengaturan "Hapus animasi" / reduced motion
- Uji lebar 360dp dan font scale 200%
- Error menyebut perbaikan (contoh: "Periksa koneksi, lalu coba lagi.")
- Label selalu kelihatan
- Jangan pakai motion 3D, morph, blur, swipe-animasi berat, glassmorphism, atau pulsa/glow terus-menerus (boros baterai, berat di HP 2 GB RAM)

Identitas visual SiagaMesh tidak meminjam palet atau merek proyek lain.

## Kriteria sukses

- 2 HP Android berbeda merek: chat grup jalan tanpa Wi-Fi/internet/SIM
- SOS dari HP A muncul di HP B dalam kurang dari 3 detik (jarak dekat)
- Absen: 3 HP, ketua lihat 3 jawaban dalam kurang dari 10 detik
- Jauh: chat tetap sampai dengan 1 HP jembatan di tengah ATAU Wi-Fi Direct ~50 m
- App tetap hidup di background minimal 30 menit (foreground service)
- APK debug bisa di-install di Moto Pad 60 Neo + 1 HP lain
- Repo public, commit Conventional Commits ID, author Ranupane saja

## Fase rilis

| Fase | Isi | Selesai jika |
| --- | --- | --- |
| 0 — Skeleton | Project Kotlin, izin, foreground service, profil lokal | App buka + nama tersimpan |
| 1 — BLE dekat | Hello peer, chat 2 HP jarak dekat | Pesan bolak-balik stabil |
| 2 — Grup + QR | Buat/join, channel key, UI chat grup | 3 HP satu grup |
| 3 — SOS + absen | Prioritas paket + UI | Demo bencana mini lulus |
| 4 — Jauh | Hop relay + Wi-Fi Direct host | Skenario jauh lulus |
| 5 — Poles | Notifikasi, battery OEM, README, release APK | Public repo + tag v0.1.0 |

## Repo & aturan kerja

- GitHub public: `ranupaners-arch/siagamesh`
- License: MIT (supaya orang bisa fork dan audit mesh)
- Commit: Conventional Commits bahasa Indonesia; author Ranupane Rimba Argana; tanpa Co-authored-by
- Coding: Claude/Codex milik Ranupane setelah PRD disetujui — Nexus tidak coding fitur
- Deploy: APK via GitHub Releases; tidak build di VPS

## Risiko

| Risiko | Dampak | Mitigasi |
| --- | --- | --- |
| OEM bunuh background | Mesh mati diam-diam | Foreground service + panduan battery whitelist |
| BLE tidak cukup untuk chat panjang | Pesan lambat/putus | Wi-Fi Direct untuk payload; BLE untuk discovery + SOS kecil |
| Izin lokasi Android untuk nearby | User bingung | Copy jelas: lokasi dipakai OS untuk scan Wi-Fi/BLE, bukan dilacak online |
| Hop flooding | Baterai dan radio penuh | ttl pendek, dedupe `msgId`, rate limit SOS |
| Ekspektasi = Covatom penuh | Scope meledak | PRD ini jadi pagar; fitur baru = fase terpisah |
