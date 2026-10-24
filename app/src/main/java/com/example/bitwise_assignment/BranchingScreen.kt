package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchingScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Nilai Max-Min", "Diskon (Nested IF)", "Diskon (Switch-Case)")

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "BranchLab",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Praktikum Percabangan: IF-ELSE, Nested IF, dan Switch-Case",
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
                            fontSize = 12.sp,
                            color = if (selectedTab == index) Purple else TextGray,
                            maxLines = 1
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
// 1. PENGGUNAAN IF-ELSE (NILAI MAXIMAL & MINIMAL)
// ==========================================
@Composable
fun IfMaxMinSection() {
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }
    var resultState by remember { mutableStateOf<Result<MaxMinResult>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Penentuan Nilai Maksimal & Minimal (IF-ELSE)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Masukkan dua bilangan, kemudian tekan tombol 'Bandingkan Angka' untuk menguji kondisi IF-ELSE.",
                fontSize = 13.sp,
                color = TextGray
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = input1,
                onValueChange = { 
                    input1 = it 
                    errorMessage = null
                },
                label = { Text("Bilangan Pertama") },
                placeholder = { Text("Contoh: 15") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = input2,
                onValueChange = { 
                    input2 = it 
                    errorMessage = null
                },
                label = { Text("Bilangan Kedua") },
                placeholder = { Text("Contoh: 25") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            Button(
                onClick = {
                    val res = BranchLabLogic.calculateMaxMin(input1, input2)
                    if (res.isSuccess) {
                        resultState = res
                        errorMessage = null
                    } else {
                        resultState = null
                        errorMessage = res.exceptionOrNull()?.message ?: "Terjadi kesalahan."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Bandingkan Angka", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Hasil Evaluasi IF-ELSE
            val res = resultState?.getOrNull()
            if (res != null) {
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
                                text = "Hasil Evaluasi IF-ELSE",
                                fontWeight = FontWeight.Bold,
                                color = Purple,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = res.message,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextDark
                        )

                        if (!res.isEqual) {
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
                                        text = BranchLabLogic.formatNumber(res.max),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF10B981) // Green
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Nilai Minimal", fontSize = 12.sp, color = TextGray)
                                    Text(
                                        text = BranchLabLogic.formatNumber(res.min),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFEF4444) // Red
                                    )
                                }
                            }
                        }
                    }
                }
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
    var resultState by remember { mutableStateOf<Result<DiscountResult>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

            Spacer(Modifier.height(8.dp))

            // Beautiful Discount Tier Guide Component with User Specified Pastel Colors
            DiscountTierGuide()

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = inputPembelian,
                onValueChange = { 
                    inputPembelian = it 
                    errorMessage = null
                },
                label = { Text("Jumlah Pembelian (Rp)") },
                placeholder = { Text("Contoh: 1250000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            Button(
                onClick = {
                    val res = BranchLabLogic.calculateNestedIfDiscount(inputPembelian)
                    if (res.isSuccess) {
                        resultState = res
                        errorMessage = null
                    } else {
                        resultState = null
                        errorMessage = res.exceptionOrNull()?.message ?: "Terjadi kesalahan."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Hitung Diskon (Nested IF)", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            val res = resultState?.getOrNull()
            if (res != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Purple.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hasil Perhitungan (Nested IF)",
                            fontWeight = FontWeight.Bold,
                            color = Purple,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        DetailRow("Total Pembelian", BranchLabLogic.formatRupiah(res.originalAmount))
                        DetailRow("Diskon yang Didapat", "${res.discountPercent}%")
                        DetailRow("Potongan Harga", "- ${BranchLabLogic.formatRupiah(res.discountAmount)}", isHighlight = true)

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
                            text = BranchLabLogic.formatRupiah(res.finalAmount),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Purple
                        )
                    }
                }
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
    var resultState by remember { mutableStateOf<Result<DiscountResult>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kalkulator Diskon (Switch-Case / When)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Logika perhitungan yang sama diimplementasikan menggunakan struktur `when` (Switch-Case Kotlin).",
                fontSize = 12.sp,
                color = TextGray
            )

            Spacer(Modifier.height(8.dp))

            // Beautiful Discount Tier Guide Component with User Specified Pastel Colors
            DiscountTierGuide()

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = inputPembelian,
                onValueChange = { 
                    inputPembelian = it 
                    errorMessage = null
                },
                label = { Text("Jumlah Pembelian (Rp)") },
                placeholder = { Text("Contoh: 1250000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            Button(
                onClick = {
                    val res = BranchLabLogic.calculateSwitchCaseDiscount(inputPembelian)
                    if (res.isSuccess) {
                        resultState = res
                        errorMessage = null
                    } else {
                        resultState = null
                        errorMessage = res.exceptionOrNull()?.message ?: "Terjadi kesalahan."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Hitung Diskon (Switch-Case)", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            val res = resultState?.getOrNull()
            if (res != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Purple.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hasil Perhitungan (Switch-Case / When)",
                            fontWeight = FontWeight.Bold,
                            color = Purple,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        DetailRow("Total Pembelian", BranchLabLogic.formatRupiah(res.originalAmount))
                        DetailRow("Diskon yang Didapat", "${res.discountPercent}%")
                        DetailRow("Potongan Harga", "- ${BranchLabLogic.formatRupiah(res.discountAmount)}", isHighlight = true)

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
                            text = BranchLabLogic.formatRupiah(res.finalAmount),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Purple
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiscountTierGuide() {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Purple.copy(alpha = 0.06f)),
        border = BorderStroke(1.dp, Purple.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Purple,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Ketentuan & Tier Diskon",
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                    fontSize = 13.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            DiscountTierRow("≥ Rp1.500.000", "Diskon 30%", Color(0xFFA8D5BA), Color(0xFF1B4332)) // #A8D5BA (Mint Green)
            Spacer(Modifier.height(6.dp))
            DiscountTierRow("Rp1.000.000 - Rp1.499.999", "Diskon 20%", Color(0xFFA9C9E8), Color(0xFF1B3A4B)) // #A9C9E8 (Soft Blue)
            Spacer(Modifier.height(6.dp))
            DiscountTierRow("Rp500.000 - Rp999.999", "Diskon 10%", Color(0xFFF4D77B), Color(0xFF5C4705)) // #F4D77B (Pastel Yellow)
            Spacer(Modifier.height(6.dp))
            DiscountTierRow("< Rp500.000", "0% (Tanpa Diskon)", Color(0xFFE5E7EB), Color(0xFF374151)) // Neutral Gray
        }
    }
}

@Composable
fun DiscountTierRow(rangeText: String, discountText: String, bgColor: Color, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "• $rangeText",
            fontSize = 12.sp,
            color = TextDark,
            fontWeight = FontWeight.Medium
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = bgColor // Solid pastel background color
        ) {
            Text(
                text = discountText,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor // Solid contrast dark text color for high readability
            )
        }
    }
}

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
