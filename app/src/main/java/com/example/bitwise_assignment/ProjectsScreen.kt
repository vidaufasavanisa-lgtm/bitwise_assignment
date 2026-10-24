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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun ProjectsScreen(
    onLearningClick: () -> Unit,
    onQuizClick: () -> Unit,
    onQuestionnaireClick: () -> Unit
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
        Spacer(Modifier.height(8.dp))
        Text(
            "Eksplorasi",
            color = TextDark,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Pilih aktivitas yang ingin kamu coba",
            color = TextGray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))

        // 1. Learning (Materi & Praktikum)
        Card(
            onClick = onLearningClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LearningIllustration()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        "Learning",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Materi & kalkulator interaktif (Pencabangan & GeoMath)",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Purple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "→",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // 2. Kuis
        Card(
            onClick = onQuizClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuizIllustration()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        "Kuis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Uji pemahamanmu lewat kuis interaktif",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Purple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "→",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // 3. Kuesioner
        Card(
            onClick = onQuestionnaireClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuestionnaireIllustration()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        "Kuesioner",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Isi kuesioner kebiasaan & preferensi",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Purple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "→",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LearningMenuScreen(
    onBranchingClick: () -> Unit,
    onGeometryClick: () -> Unit,
    onBack: () -> Unit
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Learning",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Text(
            text = "Pilih modul atau praktikum yang ingin kamu pelajari",
            fontSize = 14.sp,
            color = TextGray
        )

        Spacer(Modifier.height(24.dp))

        // Card 1: BranchLab
        Card(
            onClick = onBranchingClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BranchingIllustration()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        "BranchLab",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Praktikum IF-ELSE, Nested IF, dan Switch-Case",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Purple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "→",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Card 2: GeoMath
        Card(
            onClick = onGeometryClick,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GeoMathIllustration()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        "GeoMath",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Kalkulator geometri & bangun ruang",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Purple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "→",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun LearningIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        val color = Purple
        Canvas(modifier = Modifier.size(32.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 2.5.dp.toPx()

            val leftPage = Path().apply {
                moveTo(w * 0.5f, h * 0.25f)
                cubicTo(w * 0.35f, h * 0.2f, w * 0.2f, h * 0.25f, w * 0.12f, h * 0.22f)
                lineTo(w * 0.12f, h * 0.78f)
                cubicTo(w * 0.2f, h * 0.8f, w * 0.35f, h * 0.75f, w * 0.5f, h * 0.8f)
            }
            drawPath(leftPage, color, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

            val rightPage = Path().apply {
                moveTo(w * 0.5f, h * 0.25f)
                cubicTo(w * 0.65f, h * 0.2f, w * 0.8f, h * 0.25f, w * 0.88f, h * 0.22f)
                lineTo(w * 0.88f, h * 0.78f)
                cubicTo(w * 0.8f, h * 0.8f, w * 0.65f, h * 0.75f, w * 0.5f, h * 0.8f)
            }
            drawPath(rightPage, color, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

            drawLine(color, Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.8f), strokeWidth = strokeW)
        }
    }
}

@Composable
private fun BranchingIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        val color = Purple
        Canvas(modifier = Modifier.size(32.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 2.5.dp.toPx()

            // Draw a branching diagram icon (start node -> 2 branches)
            drawCircle(color = color, radius = w * 0.1f, center = Offset(w * 0.2f, h * 0.5f))
            
            val branchTop = Path().apply {
                moveTo(w * 0.3f, h * 0.5f)
                cubicTo(w * 0.5f, h * 0.5f, w * 0.5f, h * 0.2f, w * 0.7f, h * 0.2f)
            }
            drawPath(branchTop, color = color, style = Stroke(width = strokeW, cap = StrokeCap.Round))
            
            val branchBottom = Path().apply {
                moveTo(w * 0.3f, h * 0.5f)
                cubicTo(w * 0.5f, h * 0.5f, w * 0.5f, h * 0.8f, w * 0.7f, h * 0.8f)
            }
            drawPath(branchBottom, color = color, style = Stroke(width = strokeW, cap = StrokeCap.Round))

            drawCircle(color = color, radius = w * 0.08f, center = Offset(w * 0.8f, h * 0.2f))
            drawCircle(color = color, radius = w * 0.08f, center = Offset(w * 0.8f, h * 0.8f))
        }
    }
}

@Composable
private fun GeoMathIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        val color = Purple
        Canvas(modifier = Modifier.size(32.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 2.5.dp.toPx()

            val path = Path().apply {
                moveTo(w * 0.5f, h * 0.15f)
                lineTo(w * 0.88f, h * 0.85f)
                lineTo(w * 0.12f, h * 0.85f)
                close()
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeW))

            drawCircle(
                color = color,
                radius = w * 0.22f,
                center = Offset(w * 0.5f, h * 0.58f),
                style = Stroke(width = strokeW)
            )
        }
    }
}

@Composable
private fun QuizIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        val color = Purple
        Canvas(modifier = Modifier.size(32.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 2.5.dp.toPx()

            drawRoundRect(
                color = color,
                topLeft = Offset(w * 0.15f, h * 0.1f),
                size = Size(w * 0.7f, h * 0.8f),
                cornerRadius = CornerRadius(4.dp.toPx()),
                style = Stroke(width = strokeW)
            )

            val startX = w * 0.42f
            val endX = w * 0.72f

            drawLine(color, Offset(startX, h * 0.3f), Offset(endX, h * 0.3f), strokeWidth = strokeW, cap = StrokeCap.Round)
            drawLine(color, Offset(startX, h * 0.5f), Offset(endX, h * 0.5f), strokeWidth = strokeW, cap = StrokeCap.Round)
            drawLine(color, Offset(startX, h * 0.7f), Offset(endX, h * 0.7f), strokeWidth = strokeW, cap = StrokeCap.Round)

            val check1 = Path().apply {
                moveTo(w * 0.25f, h * 0.3f)
                lineTo(w * 0.3f, h * 0.34f)
                lineTo(w * 0.36f, h * 0.26f)
            }
            drawPath(check1, color, style = Stroke(width = strokeW, cap = StrokeCap.Round))

            val check2 = Path().apply {
                moveTo(w * 0.25f, h * 0.5f)
                lineTo(w * 0.3f, h * 0.54f)
                lineTo(w * 0.36f, h * 0.46f)
            }
            drawPath(check2, color, style = Stroke(width = strokeW, cap = StrokeCap.Round))
        }
    }
}

@Composable
private fun QuestionnaireIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        val color = Purple
        Canvas(modifier = Modifier.size(32.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 2.5.dp.toPx()
            val barWidth = w * 0.18f
            val cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx())

            drawLine(color, Offset(w * 0.1f, h * 0.88f), Offset(w * 0.9f, h * 0.88f), strokeWidth = strokeW, cap = StrokeCap.Round)

            val h1 = h * 0.35f
            drawRoundRect(
                color = color.copy(alpha = 0.5f),
                topLeft = Offset(w * 0.16f, h * 0.88f - h1),
                size = Size(barWidth, h1),
                cornerRadius = cornerRadius
            )

            val h2 = h * 0.65f
            drawRoundRect(
                color = color,
                topLeft = Offset(w * 0.41f, h * 0.88f - h2),
                size = Size(barWidth, h2),
                cornerRadius = cornerRadius
            )

            val h3 = h * 0.48f
            drawRoundRect(
                color = color.copy(alpha = 0.75f),
                topLeft = Offset(w * 0.66f, h * 0.88f - h3),
                size = Size(barWidth, h3),
                cornerRadius = cornerRadius
            )
        }
    }
}

@Composable
fun GeometryFormulasScreen(
    onBack: () -> Unit
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
