package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun GeometryIllustration(id: String) {
    val strokeColor = Purple
    val fillColor = Purple.copy(alpha = 0.15f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(140.dp, 90.dp)) {
            val w = size.width
            val h = size.height

            when (id) {
                "persegi" -> {
                    val side = minOf(w, h) * 0.65f
                    val left = (w - side) / 2f - 10f
                    val top = (h - side) / 2f
                    drawRect(
                        color = fillColor,
                        topLeft = Offset(left, top),
                        size = Size(side, side)
                    )
                    drawRect(
                        color = strokeColor,
                        topLeft = Offset(left, top),
                        size = Size(side, side),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
                "segitiga" -> {
                    val path = Path().apply {
                        moveTo(w / 2f, h * 0.08f)
                        lineTo(w * 0.78f, h * 0.75f)
                        lineTo(w * 0.22f, h * 0.75f)
                        close()
                    }
                    drawPath(path, color = fillColor)
                    drawPath(path, color = strokeColor, style = Stroke(width = 3.dp.toPx()))
                    drawLine(
                        color = strokeColor.copy(alpha = 0.6f),
                        start = Offset(w / 2f, h * 0.08f),
                        end = Offset(w / 2f, h * 0.75f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                "lingkaran" -> {
                    val radius = minOf(w, h) * 0.35f
                    val center = Offset(w / 2f, h / 2f)
                    drawCircle(color = fillColor, radius = radius, center = center)
                    drawCircle(color = strokeColor, radius = radius, center = center, style = Stroke(width = 3.dp.toPx()))
                    drawLine(
                        color = strokeColor,
                        start = center,
                        end = Offset(center.x + radius, center.y),
                        strokeWidth = 2.dp.toPx()
                    )
                    drawCircle(color = strokeColor, radius = 3.dp.toPx(), center = center)
                }
                "tabung" -> {
                    val rx = w * 0.25f
                    val ry = h * 0.12f
                    val cx = w / 2f
                    val topCy = h * 0.3f
                    val botCy = h * 0.8f

                    drawRect(
                        color = fillColor,
                        topLeft = Offset(cx - rx, topCy),
                        size = Size(rx * 2f, botCy - topCy)
                    )
                    drawLine(color = strokeColor, start = Offset(cx - rx, topCy), end = Offset(cx - rx, botCy), strokeWidth = 3.dp.toPx())
                    drawLine(color = strokeColor, start = Offset(cx + rx, topCy), end = Offset(cx + rx, botCy), strokeWidth = 3.dp.toPx())

                    drawOval(
                        color = fillColor,
                        topLeft = Offset(cx - rx, botCy - ry),
                        size = Size(rx * 2f, ry * 2f)
                    )
                    drawOval(
                        color = strokeColor,
                        topLeft = Offset(cx - rx, botCy - ry),
                        size = Size(rx * 2f, ry * 2f),
                        style = Stroke(width = 3.dp.toPx())
                    )

                    drawOval(
                        color = fillColor,
                        topLeft = Offset(cx - rx, topCy - ry),
                        size = Size(rx * 2f, ry * 2f)
                    )
                    drawOval(
                        color = strokeColor,
                        topLeft = Offset(cx - rx, topCy - ry),
                        size = Size(rx * 2f, ry * 2f),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }

        // Overlay Text Labels
        when (id) {
            "persegi" -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.width(95.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("s", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                }
            }
            "segitiga" -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 'a' placed slightly lower below the base line
                    Box(modifier = Modifier.fillMaxSize().padding(bottom = 6.dp), contentAlignment = Alignment.BottomCenter) {
                        Text("a", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                    // 't' at center height
                    Box(modifier = Modifier.fillMaxSize().padding(bottom = 25.dp, start = 18.dp), contentAlignment = Alignment.Center) {
                        Text("t", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                }
            }
            "lingkaran" -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize().padding(bottom = 18.dp), contentAlignment = Alignment.Center) {
                        Text("r", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                }
            }
            "tabung" -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 'r' on top radius
                    Box(modifier = Modifier.fillMaxSize().padding(bottom = 45.dp), contentAlignment = Alignment.Center) {
                        Text("r", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                    // 't' on the right side OUTSIDE the cylinder
                    Box(modifier = Modifier.fillMaxSize().padding(start = 105.dp), contentAlignment = Alignment.Center) {
                        Text("t", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                }
            }
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
            Spacer(Modifier.height(8.dp))

            // Ilustrasi Geometri dengan label rumus
            GeometryIllustration(id = item.id)

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
