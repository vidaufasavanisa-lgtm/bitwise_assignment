# Presentasi Fitur BranchLab — Aplikasi Android Kotlin

Dokumen ini berisi rancangan slide presentasi untuk fitur **BranchLab**, lengkap dengan penjelasan logika, potongan kode penting, dan uraian singkat untuk setiap bagian.

---

## Slide 1: Judul & Pengantar BranchLab

### **Judul Slide:**
> **BranchLab: Eksplorasi & Praktikum Percabangan dalam Kotlin**

### **Poin Utama:**
- **Latar Belakang:** Bagian dari modul *Learning* aplikasi Android yang berfokus pada pemahaman struktur kontrol alur (percabangan).
- **Tujuan:** Menyediakan wadah interaktif bagi pengguna untuk mempelajari dan mempraktikkan tiga jenis percabangan utama di Kotlin:
  1. `if-else` (Penentuan Nilai Maksimal & Minimal)
  2. Nested `if` (Kalkulator Diskon Bertingkat)
  3. `when` / Switch-Case (Kalkulator Diskon Alternatif)
- **Desain & Konsistensi:** Dibangun selaras dengan palet warna aplikasi, tipografi Material 3, dan dukungan tema responsif (termasuk palet chip pastel solid: `#A8D5BA`, `#A9C9E8`, `#F4D77B`).

---

## Slide 2: Arsitektur & Pemisahan Logika (Separation of Concerns)

### **Judul Slide:**
> **Arsitektur & Pemisahan Logika (Clean Architecture)**

### **Poin Utama:**
- **Pemisahan Modul:**
  - **`BranchLabLogic.kt`**: Berisi seluruh fungsi perhitungan, logika perbandingan, penentuan diskon, dan validasi input.
  - **`BranchingScreen.kt`**: Berfokus murni pada antarmuka pengguna (UI), tab navigasi, form input, dan menampilkan hasil secara interaktif.
- **Keunggulan:**
  - Memudahkan pengujian unit (*unit testing*).
  - Kode lebih terstruktur, modular, dan mudah dipelihara.

---

## Slide 3: Fitur 1 — Penentuan Nilai Maksimal & Minimal (IF-ELSE)

### **Judul Slide:**
> **Fitur 1: Penentuan Nilai Maksimal & Minimal (`IF-ELSE`)**

### **Penjelasan Logika:**
- Menerima dua input bilangan dari pengguna.
- Menguji kondisi menggunakan percabangan bertingkat `if-else`:
  - Jika bilangan pertama $>$ bilangan kedua $\rightarrow$ bilangan pertama adalah maksimum, bilangan kedua adalah minimum.
  - Jika bilangan kedua $>$ bilangan pertama $\rightarrow$ bilangan kedua adalah maksimum, bilangan pertama adalah minimum.
  - **Kondisi Khusus:** Jika kedua bilangan bernilai sama $\rightarrow$ menampilkan pesan informasi bahwa kedua bilangan memiliki nilai yang sama.
- **Validasi:** Mencegah input kosong atau format bukan angka.

### **Potongan Kode Penting (`BranchLabLogic.kt`):**
```kotlin
fun calculateMaxMin(input1: String, input2: String): Result<MaxMinResult> {
    if (input1.isBlank() || input2.isBlank()) {
        return Result.failure(IllegalArgumentException("Kedua bilangan tidak boleh kosong."))
    }
    val num1 = input1.toDoubleOrNull()
    val num2 = input2.toDoubleOrNull()
    if (num1 == null || num2 == null) {
        return Result.failure(IllegalArgumentException("Format angka tidak valid."))
    }

    val max: Double
    val min: Double
    val message: String
    val isEqual: Boolean

    if (num1 > num2) {
        max = num1
        min = num2
        message = "Bilangan pertama (${formatNumber(num1)}) lebih besar dari bilangan kedua (${formatNumber(num2)})."
        isEqual = false
    } else if (num2 > num1) {
        max = num2
        min = num1
        message = "Bilangan kedua (${formatNumber(num2)}) lebih besar dari bilangan pertama (${formatNumber(num1)})."
        isEqual = false
    } else {
        max = num1
        min = num2
        message = "Kedua bilangan memiliki nilai yang sama yaitu ${formatNumber(num1)}."
        isEqual = true
    }
    return Result.success(MaxMinResult(max, min, message, isEqual))
}
```

---

## Slide 4: Fitur 2 — Kalkulator Diskon (Nested IF)

### **Judul Slide:**
> **Fitur 2: Kalkulator Diskon Bertingkat (`Nested IF`)**

### **Penjelasan Logika & Aturan Tier Diskon:**
- Menghitung diskon berdasarkan jumlah total pembelian dalam Rupiah secara hierarkis (*nested*):
  - Pembelian $\ge$ Rp1.500.000 $\rightarrow$ Diskon **30%** (Chip `#A8D5BA`)
  - Rp1.000.000 $\le$ Pembelian $<$ Rp1.500.000 $\rightarrow$ Diskon **20%** (Chip `#A9C9E8`)
  - Rp500.000 $\le$ Pembelian $<$ Rp1.000.000 $\rightarrow$ Diskon **10%** (Chip `#F4D77B`)
  - Pembelian $<$ Rp500.000 $\rightarrow$ Diskon **0%** (Tanpa Diskon)
- **Validasi:** Validasi input kosong, format angka, dan nilai negatif ($<$ 0).

### **Potongan Kode Penting (`Nested IF`):**
```kotlin
fun calculateNestedIfDiscount(inputPembelian: String): Result<DiscountResult> {
    val amount = inputPembelian.toDoubleOrNull() ?: return Result.failure(...)

    // Implementasi Nested IF secara eksplisit
    val discountPercent = if (amount >= 1500000.0) {
        30
    } else {
        if (amount >= 1000000.0) {
            20
        } else {
            if (amount >= 500000.0) {
                10
            } else {
                0
            }
        }
    }
    val discountAmount = amount * discountPercent / 100.0
    val finalAmount = amount - discountAmount
    return Result.success(DiscountResult(amount, discountPercent, discountAmount, finalAmount))
}
```

---

## Slide 5: Fitur 3 — Kalkulator Diskon (`Switch-Case` / `when`)

### **Judul Slide:**
> **Fitur 3: Kalkulator Diskon Alternatif (`Switch-Case` via `when`)**

### **Penjelasan Logika:**
- Menerapkan aturan diskon yang sama persis dengan Fitur 2, namun menggunakan struktur kontrol **`when`** khas Kotlin (padanan `switch-case` pada bahasa pemrograman lain).
- **Keunggulan `when`:**
  - Sintaks lebih bersih (*clean code*), mudah dibaca (*readable*), dan terhindar dari *callback/nesting hell*.
- **Konsistensi:** Memastikan hasil perhitungan diskon, nominal potongan, dan total pembayaran antara *Nested IF* dan *Switch-Case* selalu identik dan konsisten.

### **Potongan Kode Penting (`when` / Switch-Case):**
```kotlin
fun calculateSwitchCaseDiscount(inputPembelian: String): Result<DiscountResult> {
    val amount = inputPembelian.toDoubleOrNull() ?: return Result.failure(...)

    // Implementasi Switch-Case menggunakan Kotlin 'when'
    val discountPercent = when {
        amount >= 1500000.0 -> 30
        amount >= 1000000.0 -> 20
        amount >= 500000.0 -> 10
        else -> 0
    }
    val discountAmount = amount * discountPercent / 100.0
    val finalAmount = amount - discountAmount
    return Result.success(DiscountResult(amount, discountPercent, discountAmount, finalAmount))
}
```

---

## Slide 6: Kesimpulan & Demo Aplikasi

### **Judul Slide:**
> **Kesimpulan & Demo Fitur**

### **Poin Utama:**
1. **Penerapan Praktis:** BranchLab berhasil menjembatani teori percabangan pemrograman (`if-else`, `nested if`, `when`) ke dalam aplikasi Android interaktif yang nyata.
2. **Pengalaman Pengguna (UX):**
   - Panduan tier diskon visual dengan warna pastel solid (`#A8D5BA`, `#A9C9E8`, `#F4D77B`).
   - Validasi input proaktif dengan pesan kesalahan yang jelas.
3. **Kualitas Kode:** Berbasis arsitektur bersih, teruji, dan terintegrasi mulus dengan navigasi utama aplikasi.

---
*Dokumen slide ini siap digunakan untuk bahan presentasi atau laporan proyek.*
