package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun ProjectsScreen(
    onGeometryClick: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp)
    ) {
        AppHeader(onOpenDrawer = onOpenDrawer)
        Spacer(Modifier.height(24.dp))

        Text(
            "PROJECTS",
            color = Purple,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Daftar Proyek",
            color = TextDark,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Eksplorasi modul dan aplikasi interaktif buatan kelompok Bitwise.",
            color = TextGray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))

        // Project Card: Rumus Geometri
        Card(
            onClick = onGeometryClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "📐 GeoMath",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Kalkulator geometri untuk menghitung luas bangun datar & bangun ruang",
                    fontSize = 14.sp,
                    color = TextGray,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Purple)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "Buka Modul →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GeometryFormulasScreen(
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp)
    ) {
        AppHeader(onOpenDrawer = onOpenDrawer)
        Spacer(Modifier.height(24.dp))

        Text(
            "← Kembali",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = TextDark,
            modifier = Modifier.clickable { onBack() }
        )
        Spacer(Modifier.height(20.dp))

        Text(
            "GeoMath",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Kalkulator geometri untuk menghitung luas bangun datar & bangun ruang",
            fontSize = 14.sp,
            color = TextGray
        )
        Spacer(Modifier.height(24.dp))

        // Sections
        FormulaSection(
            title = "A. Bangun Datar",
            items = listOf(
                FormulaItem("persegi", "Luas Persegi", "Luas = s × s"),
                FormulaItem("segitiga", "Luas Segitiga", "Luas = ½ × a × t"),
                FormulaItem("lingkaran", "Luas Lingkaran", "Luas = π × r²")
            )
        )

        Spacer(Modifier.height(20.dp))

        FormulaSection(
            title = "B. Bangun Ruang",
            items = listOf(
                FormulaItem("tabung", "Isi Tabung", "Volume = π × r² × t")
            )
        )
    }
}

data class FormulaItem(val id: String, val name: String, val formula: String)

@Composable
fun FormulaSection(title: String, items: List<FormulaItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            FormulaCalculatorCard(item = item)
        }
    }
}

@Composable
fun FormulaCalculatorCard(item: FormulaItem) {
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.formula,
                fontSize = 14.sp,
                color = TextGray,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(12.dp))

            when (item.id) {
                "persegi" -> {
                    OutlinedTextField(
                        value = input1,
                        onValueChange = { input1 = it },
                        label = { Text("Panjang Sisi (s)", color = TextGray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBgColor,
                            unfocusedContainerColor = CardBgColor,
                            focusedBorderColor = Purple,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )
                }
                "segitiga" -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = input1,
                            onValueChange = { input1 = it },
                            label = { Text("Alas (a)", color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardBgColor,
                                unfocusedContainerColor = CardBgColor,
                                focusedBorderColor = Purple,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            )
                        )
                        OutlinedTextField(
                            value = input2,
                            onValueChange = { input2 = it },
                            label = { Text("Tinggi (t)", color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardBgColor,
                                unfocusedContainerColor = CardBgColor,
                                focusedBorderColor = Purple,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            )
                        )
                    }
                }
                "lingkaran" -> {
                    OutlinedTextField(
                        value = input1,
                        onValueChange = { input1 = it },
                        label = { Text("Jari-jari (r)", color = TextGray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBgColor,
                            unfocusedContainerColor = CardBgColor,
                            focusedBorderColor = Purple,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )
                }
                "tabung" -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = input1,
                            onValueChange = { input1 = it },
                            label = { Text("Jari-jari (r)", color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardBgColor,
                                unfocusedContainerColor = CardBgColor,
                                focusedBorderColor = Purple,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            )
                        )
                        OutlinedTextField(
                            value = input2,
                            onValueChange = { input2 = it },
                            label = { Text("Tinggi (t)", color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardBgColor,
                                unfocusedContainerColor = CardBgColor,
                                focusedBorderColor = Purple,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (result != null) {
                    Text(
                        text = "Hasil: $result",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Purple
                    )
                } else {
                    Spacer(Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        val v1 = input1.toDoubleOrNull() ?: 0.0
                        val v2 = input2.toDoubleOrNull() ?: 0.0
                        result = when (item.id) {
                            "persegi" -> String.format(Locale.getDefault(), "%.2f", v1 * v1)
                            "segitiga" -> String.format(Locale.getDefault(), "%.2f", 0.5 * v1 * v2)
                            "lingkaran" -> String.format(Locale.getDefault(), "%.2f", Math.PI * v1 * v1)
                            "tabung" -> String.format(Locale.getDefault(), "%.2f", Math.PI * v1 * v1 * v2)
                            else -> "0"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Hitung", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
