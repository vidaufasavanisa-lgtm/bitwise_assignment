package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchingScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Nilai Max-Min", "Kalkulator Diskon", "Kalkulator Diskon")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
    ) {
        // Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fitur Pencabangan",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Kalkulator nilai makmium dan minimum, serta diskon",
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }

        // Tab Selector
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = CardBgColor,
            contentColor = Purple
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (selectedTab == index) Purple else TextGray
                        )
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> IfMaxMinSection()
                1 -> NestedIfDiscountSection()
                2 -> SwitchCaseDiscountSection()
            }
        }
    }
}

// ==========================================
// 1. PENGGUNAAN IF (NILAI MAXIMAL & MINIMAL)
// ==========================================
@Composable
fun IfMaxMinSection() {
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }

    val num1 = input1.toDoubleOrNull()
    val num2 = input2.toDoubleOrNull()

    // Pengujian kondisi IF
    val hasValidInput = num1 != null && num2 != null
    var maxVal = 0.0
    var minVal = 0.0
    var statusText = ""

    if (num1 != null && num2 != null) {
        if (num1 > num2) {
            maxVal = num1
            minVal = num2
                        statusText = "Ya (Yes), Bilangan 1 ($num1) lebih besar dari Bilangan 2 ($num2)"
        } else if (num2 > num1) {
            maxVal = num2
            minVal = num1
            statusText = "Bilangan ${formatNumber(num2)} lebih besar dari bilangan ${formatNumber(num1)}"
        } else {
            maxVal = num1
            minVal = num2
            statusText = "Bilangan ${formatNumber(num1)} sama besar dengan bilangan ${formatNumber(num2)}"
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Penentuan Nilai Maksimal & Minimal (IF)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Input 2 bilangan, kemudian sistem menguji kondisi untuk menentukan nilai terbesar dan terkecil.",
                fontSize = 13.sp,
                color = TextGray
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = input1,
                onValueChange = { input1 = it },
                label = { Text("Bilangan Pertama") },
                placeholder = { Text("Contoh: 13") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = Purple
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = input2,
                onValueChange = { input2 = it },
                label = { Text("Bilangan Kedua") },
                placeholder = { Text("Contoh: 7") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = Purple
                )
            )

            Spacer(Modifier.height(16.dp))

            // Hasil Evaluasi IF
            if (hasValidInput) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Purple.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Purple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Hasil Evaluasi",
                                fontWeight = FontWeight.Bold,
                                color = Purple,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = statusText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = BorderColor
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Nilai Maksimal", fontSize = 12.sp, color = TextGray)
                                Text(
                                    text = formatNumber(maxVal),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF10B981) // Green
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Nilai Minimal", fontSize = 12.sp, color = TextGray)
                                Text(
                                    text = formatNumber(minVal),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFEF4444) // Red
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Silakan masukkan kedua bilangan di atas untuk melihat hasilnya.",
                    fontSize = 12.sp,
                    color = TextGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// ==========================================
// 2. PENGGUNAAN NESTED IF (DISKON)
// ==========================================

@Composable
fun NestedIfDiscountSection() {
    var inputPembelian by remember { mutableStateOf("") }

    val totalPembelian = inputPembelian.toDoubleOrNull()
    val inputValid = totalPembelian != null && totalPembelian > 0

    val diskonPersen = if (inputValid) {
        if (totalPembelian!! >= 1500000) {
            30
        } else {
            if (totalPembelian >= 1000000) {
                20
            } else {
                if (totalPembelian >= 500000) {
                    10
                } else {
                    0
                }
            }
        }
    } else {
        0
    }

    val nilaiDiskon =
        if (inputValid) totalPembelian!! * diskonPersen / 100.0 else 0.0

    val totalBayar =
        if (inputValid) totalPembelian!! - nilaiDiskon else 0.0

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kalkulator Diskon (Nested IF)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Masukkan jumlah pembelian untuk mengetahui diskon dan total pembayaran.",
                fontSize = 13.sp,
                color = TextGray
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = inputPembelian,
                onValueChange = { inputPembelian = it },
                label = { Text("Jumlah Pembelian (Rp)") },
                placeholder = { Text("Contoh: 1250000") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = Purple,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark
                )
            )

            Spacer(Modifier.height(16.dp))

            if (inputValid) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Purple.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hasil Perhitungan",
                            fontWeight = FontWeight.Bold,
                            color = Purple,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        DetailRow(
                            "Total Pembelian",
                            formatRupiah(totalPembelian!!)
                        )

                        DetailRow(
                            "Diskon yang Didapat",
                            "$diskonPersen%"
                        )

                        DetailRow(
                            "Potongan Harga",
                            "- ${formatRupiah(nilaiDiskon)}",
                            isHighlight = true
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = BorderColor
                        )

                        Text(
                            text = "Total yang Harus Dibayar",
                            fontSize = 13.sp,
                            color = TextGray
                        )

                        Text(
                            text = formatRupiah(totalBayar),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Purple
                        )
                    }
                }
            } else {
                Text(
                    text = "Masukkan jumlah pembelian lebih dari Rp0 untuk melihat hasil.",
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }
    }
}


// ==========================================
// 3. PENGGUNAAN SWITCH-CASE / WHEN (DISKON)
// ==========================================

@Composable
fun SwitchCaseDiscountSection() {
    var inputPembelian by remember { mutableStateOf("") }

    val totalPembelian = inputPembelian.toDoubleOrNull()
    val inputValid = totalPembelian != null && totalPembelian > 0

    val diskonPersen = if (inputValid) {
        when {
            totalPembelian!! >= 1500000 -> 30
            totalPembelian >= 1000000 -> 20
            totalPembelian >= 500000 -> 10
            else -> 0
        }
    } else {
        0
    }

    val nilaiDiskon =
        if (inputValid) totalPembelian!! * diskonPersen / 100.0 else 0.0

    val totalBayar =
        if (inputValid) totalPembelian!! - nilaiDiskon else 0.0

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kalkulator Diskon (Switch-Case)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Masukkan jumlah pembelian untuk mengetahui diskon dan total pembayaran.",
                fontSize = 13.sp,
                color = TextGray
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = inputPembelian,
                onValueChange = { inputPembelian = it },
                label = { Text("Jumlah Pembelian (Rp)") },
                placeholder = { Text("Contoh: 1250000") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = Purple,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark
                )
            )

            Spacer(Modifier.height(16.dp))

            if (inputValid) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Purple.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hasil Perhitungan",
                            fontWeight = FontWeight.Bold,
                            color = Purple,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        DetailRow(
                            "Total Pembelian",
                            formatRupiah(totalPembelian!!)
                        )

                        DetailRow(
                            "Diskon yang Didapat",
                            "$diskonPersen%"
                        )

                        DetailRow(
                            "Potongan Harga",
                            "- ${formatRupiah(nilaiDiskon)}",
                            isHighlight = true
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = BorderColor
                        )

                        Text(
                            text = "Total yang Harus Dibayar",
                            fontSize = 13.sp,
                            color = TextGray
                        )

                        Text(
                            text = formatRupiah(totalBayar),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Purple
                        )
                    }
                }
            } else {
                Text(
                    text = "Masukkan jumlah pembelian lebih dari Rp0 untuk melihat hasil.",
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }
    }
}


// ==========================================
// HELPER COMPONENTS & UTILS
// ==========================================
@Composable
fun DetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextGray)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlight) Color(0xFF10B981) else TextDark
        )
    }
}

fun formatNumber(number: Double): String {
    return if (number % 1.0 == 0.0) {
        number.toLong().toString()
    } else {
        String.format(Locale.US, "%.2f", number)
    }
}

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    return format.format(amount).replace(",00", "")
}
