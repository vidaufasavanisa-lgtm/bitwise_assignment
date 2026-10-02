# Project Context: Bitwise Assignment

## Tujuan dan Gambaran Umum
Aplikasi ini adalah sebuah aplikasi Android sederhana yang dibuat untuk memenuhi tugas mata kuliah Kerja Kelompok (Kerkom). Tujuan utamanya adalah untuk menampilkan profil singkat dari 4 anggota tim (bernama "Bitwise"). Pengguna dapat melihat daftar anggota pada halaman utama, mencari anggota berdasarkan nama, dan menekan salah satu anggota untuk melihat detail biodatanya.

## Teknologi yang Digunakan
- **Bahasa Pemrograman**: Kotlin
- **Platform**: Android
- **UI Framework**: Jetpack Compose (Material 3)

## Struktur File Penting
Semua file kode utama berada di dalam folder `app/src/main/java/com/example/bitwise_assignment/`:
- `MainActivity.kt`: Entry point aplikasi yang mengatur alur navigasi halaman (berpindah antara halaman utama dan detail profil).
- `HomeScreen.kt`: Layar utama yang menampilkan daftar anggota tim dalam bentuk grid (2 kolom). Memiliki fitur pencarian (*search bar*) sederhana yang memfilter daftar berdasarkan nama panggilan.
- `ProfileScreen.kt`: Layar detail profil anggota. Menampilkan nama panggilan, NIM, serta _card_ biodata lengkap (Nama Lengkap, Nama Panggilan, Hobi, Cita-cita, dan Motto). Hobi ditampilkan secara visual dalam bentuk deretan *tag*/*pill* (opsi *scroll* horizontal).
- `Member.kt`: File *data layer* yang berisi pendefinisian data class `Member` dan *dummy data* (`MemberData`). Aplikasi ini tidak menggunakan database sungguhan, semua data bersifat statis (*hardcoded*).

## Tampilan dan Alur Navigasi Aplikasi
Aplikasi memiliki alur navigasi yang sangat sederhana dengan dua layar utama:
1. **Home Screen**: Saat aplikasi dibuka, pengguna akan melihat halaman yang berisi logo "Bitwise", teks sambutan, kolom pencarian, dan daftar kartu anggota (berisi avatar inisial nama, nama panggilan, dan NIM).
2. **Profile Screen**: Jika kartu anggota di halaman Home diklik, pengguna akan diarahkan ke halaman Profile Screen yang menampilkan informasi lebih lengkap terkait anggota tersebut. Terdapat tombol "Kembali" untuk kembali ke Home Screen.

## Fitur Daftar Anggota dan Detail Anggota
- **Daftar Anggota**: Menampilkan 4 anggota tim (Alya, Bima, Citra, Daffa). Terdapat kotak pencarian di bagian atas untuk memfilter anggota berdasarkan nama panggilan.
- **Detail Anggota**: 
  - **Identitas Utama**: Avatar berupa inisial huruf besar dari nama panggilan, nama panggilan besar, dan NIM.
  - **Biodata**: Sebuah _card_ putih bergaris tipis yang memuat:
    - Nama Lengkap (String)
    - Nama Panggilan (String)
    - Hobi (ditampilkan sebagai deretan *tags/pill* berwarna ungu)
    - Cita-cita (String)
    - Motto (String)

## Aturan Coding dan Panduan untuk Pengembangan Selanjutnya
1. **Jangan asumsikan fungsionalitas yang tidak ada**: Aplikasi ini menggunakan arsitektur sederhana dengan _state_ lokal dan *dummy data* (`Member.kt`). Jangan mencoba mengimplementasikan sistem *database* lokal (seperti Room) atau pemanggilan API jaringan (*network call*) tanpa permintaan eksplisit.
2. **Konsistensi UI (Jetpack Compose)**: Seluruh _layout_, pewarnaan, dan komponen UI ditulis murni dengan Jetpack Compose. Jika membuat halaman atau _widget_ baru, gunakan komponen Material 3 yang sudah ada dan hindari XML.
3. **Pewarnaan**: Terdapat warna-warna *custom* yang dideklarasikan (seperti `BgColor`, `Purple`, `PurpleDark`, `TextDark`, dll) di dalam `HomeScreen.kt`. Selalu manfaatkan palet warna ini agar _style_ tetap serasi.
4. **Modifikasi Data**: Jika ingin menambah atau mengubah atribut profil anggota, pastikan untuk menyesuaikan `data class Member` dan juga `MemberData` di dalam `Member.kt`.
