package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

// =========================
// LOGIKA (sesuai ketentuan tugas)
// =========================

/** 1. Bilangan bulat increasing 20 angka -> pakai FOR */
fun generateBilanganBulat(): List<Int> {
    val hasil = mutableListOf<Int>()
    for (i in 1..20) {
        hasil.add(i)
    }
    return hasil
}

/** 2. Bilangan ganjil increasing 20 angka -> pakai WHILE */
fun generateBilanganGanjil(): List<Int> {
    val hasil = mutableListOf<Int>()
    var angka = 1
    var count = 0
    while (count < 20) {
        hasil.add(angka)
        angka += 2
        count++
    }
    return hasil
}

/** 3. Bilangan Fibonacci 20 angka -> pakai DO-WHILE */
fun generateFibonacci(): List<Long> {
    val hasil = mutableListOf<Long>()
    var a = 0L
    var b = 1L
    var count = 0
    do {
        hasil.add(a)
        val next = a + b
        a = b
        b = next
        count++
    } while (count < 20)
    return hasil
}

/** 4. Bilangan prima 20 angka pertama (increasing) */
fun generateBilanganPrima(): List<Int> {
    val hasil = mutableListOf<Int>()
    var angka = 2
    while (hasil.size < 20) {
        var prima = true
        var i = 2
        while (i * i <= angka) {
            if (angka % i == 0) {
                prima = false
                break
            }
            i++
        }
        if (prima) hasil.add(angka)
        angka++
    }
    return hasil
}

// =========================
// DATA MODEL
// =========================

data class NumberItem(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val cornerRadius: Int,
    val generator: () -> List<Number>
)

val numberItems = listOf(
    NumberItem(
        id = "bulat",
        name = "Bilangan Bulat",
        title = "20 Bilangan Bulat Pertama!",
        description = "Bilangan bulat berurutan dari 1 hingga 20",
        cornerRadius = 14,
        generator = { generateBilanganBulat() }
    ),
    NumberItem(
        id = "ganjil",
        name = "Bilangan Ganjil",
        title = "20 Bilangan Ganjil Pertama!",
        description = "Bilangan ganjil berurutan mulai dari 1",
        cornerRadius = 18,
        generator = { generateBilanganGanjil() }
    ),
    NumberItem(
        id = "fibonacci",
        name = "Bilangan Fibonacci",
        title = "20 Suku Fibonacci Pertama!",
        description = "Setiap suku adalah jumlah dua suku sebelumnya",
        cornerRadius = 10,
        generator = { generateFibonacci() }
    ),
    NumberItem(
        id = "prima",
        name = "Bilangan Prima",
        title = "20 Bilangan Prima Pertama!",
        description = "Bilangan yang hanya habis dibagi 1 dan dirinya sendiri",
        cornerRadius = 18,
        generator = { generateBilanganPrima() }
    )
)

// =========================
// CARD MENU (dipakai di LearningMenuScreen)
// =========================

@Composable
fun NumberMenuCards(onNumberClick: (String) -> Unit) {
    numberItems.forEachIndexed { index, item ->
        if (index > 0) Spacer(Modifier.height(14.dp))

        Card(
            onClick = { onNumberClick(item.id) },
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
                NumberIllustration(id = item.id)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        item.description,
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

// =========================
// HALAMAN DETAIL PER BILANGAN
// =========================

@Composable
fun NumberDetailScreen(type: String, onBack: () -> Unit) {
    val item = numberItems.firstOrNull { it.id == type } ?: numberItems.first()
    val numbers = remember(item.id) { item.generator() }
    var visibleCount by remember { mutableIntStateOf(0) }

    // Angka muncul satu per satu saat halaman dibuka
    LaunchedEffect(item.id) {
        visibleCount = 0
        for (i in 1..numbers.size) {
            delay(40)
            visibleCount = i
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Header dengan tombol kembali & chip nama bilangan
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = TextDark
                )
            }
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Purple.copy(alpha = 0.15f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    item.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title,
                    fontSize = 30.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    item.description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextGray
                )
            }
            Spacer(Modifier.width(12.dp))
            NumberIllustration(id = item.id, size = 80.dp)
        }

        Spacer(Modifier.height(24.dp))

        // Card grid angka
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                numbers.chunked(5).forEachIndexed { rowIndex, rowItems ->
                    if (rowIndex > 0) Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEachIndexed { colIndex, value ->
                            val index = rowIndex * 5 + colIndex
                            Box(modifier = Modifier.weight(1f)) {
                                NumberCell(
                                    value = value.toString(),
                                    highlighted = index == numbers.lastIndex,
                                    shown = index < visibleCount,
                                    cornerRadius = item.cornerRadius
                                )
                            }
                        }
                        repeat(5 - rowItems.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Ringkasan total
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Purple.copy(alpha = 0.12f))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Purple),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${numbers.size}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    "Total: ${numbers.size} Angka",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    "Dari ${numbers.first()} hingga ${numbers.last()}",
                    fontSize = 13.sp,
                    color = TextGray
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.height(16.dp))
    }
}

// =========================
// CELL ANGKA (tanpa badge urutan)
// =========================

@Composable
private fun NumberCell(
    value: String,
    highlighted: Boolean,
    shown: Boolean,
    cornerRadius: Int
) {
    val shape = RoundedCornerShape(cornerRadius.dp)
    val bg = if (highlighted) Purple else Purple.copy(alpha = 0.12f)
    val textColor = if (highlighted) Color.White else TextDark

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .alpha(if (shown) 1f else 0.15f)
            .clip(shape)
            .background(bg)
            .border(1.dp, Purple.copy(alpha = 0.35f), shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = when {
                value.length >= 4 -> 14.sp
                value.length == 3 -> 16.sp
                else -> 18.sp
            },
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}

// =========================
// ILUSTRASI
// =========================

private fun DrawScope.arrowArc(
    color: Color,
    topLeft: Offset,
    arcSize: Size,
    startAngle: Float,
    sweep: Float,
    strokeW: Float
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeW, cap = StrokeCap.Round)
    )
    val cx = topLeft.x + arcSize.width / 2f
    val cy = topLeft.y + arcSize.height / 2f
    val r = arcSize.width / 2f
    val rad = Math.toRadians((startAngle + sweep).toDouble())
    val nx = cos(rad).toFloat()
    val ny = sin(rad).toFloat()
    val ex = cx + r * nx
    val ey = cy + r * ny
    val tx = -ny
    val ty = nx
    val head = strokeW * 2.2f
    val head2 = head * 0.8f
    val p = Path().apply {
        moveTo(ex + tx * head, ey + ty * head)
        lineTo(ex - nx * head2, ey - ny * head2)
        lineTo(ex + nx * head2, ey + ny * head2)
        close()
    }
    drawPath(p, color)
}

private fun DrawScope.sparkle(color: Color, c: Offset, r: Float) {
    val p = Path().apply {
        moveTo(c.x, c.y - r)
        quadraticBezierTo(c.x, c.y, c.x + r, c.y)
        quadraticBezierTo(c.x, c.y, c.x, c.y + r)
        quadraticBezierTo(c.x, c.y, c.x - r, c.y)
        quadraticBezierTo(c.x, c.y, c.x, c.y - r)
        close()
    }
    drawPath(p, color)
}

@Composable
fun NumberIllustration(
    modifier: Modifier = Modifier,
    id: String = "bulat",
    size: Dp = 56.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        val color = Purple
        Canvas(modifier = Modifier.size(size * 0.58f)) {
            val w = this.size.width
            val h = this.size.height
            val strokeW = 2.5.dp.toPx()
            val cap = StrokeCap.Round

            when (id) {
                // dua panah melingkar
                "bulat" -> {
                    val d = w * 0.78f
                    val tl = Offset((w - d) / 2f, (h - d) / 2f)
                    arrowArc(color, tl, Size(d, d), 200f, 130f, strokeW)
                    arrowArc(color, tl, Size(d, d), 20f, 130f, strokeW)
                }
                // satu panah melingkar besar + titik tengah
                "ganjil" -> {
                    val d = w * 0.8f
                    val tl = Offset((w - d) / 2f, (h - d) / 2f)
                    arrowArc(color, tl, Size(d, d), 120f, 280f, strokeW)
                    drawCircle(color.copy(alpha = 0.5f), radius = w * 0.14f, center = Offset(w / 2f, h / 2f))
                }
                // spiral golden ratio
                "fibonacci" -> {
                    drawRect(
                        color = color.copy(alpha = 0.35f),
                        topLeft = Offset(w * 0.08f, h * 0.12f),
                        size = Size(w * 0.84f, h * 0.76f),
                        style = Stroke(width = strokeW * 0.6f)
                    )
                    val p = Path().apply {
                        moveTo(w * 0.15f, h * 0.55f)
                        cubicTo(w * 0.15f, h * 0.2f, w * 0.7f, h * 0.1f, w * 0.85f, h * 0.45f)
                        cubicTo(w * 0.95f, h * 0.8f, w * 0.45f, h * 0.9f, w * 0.4f, h * 0.65f)
                        cubicTo(w * 0.38f, h * 0.5f, w * 0.55f, h * 0.45f, w * 0.58f, h * 0.55f)
                    }
                    drawPath(p, color, style = Stroke(width = strokeW, cap = cap))
                }
                // kilau bintang
                "prima" -> {
                    sparkle(color, Offset(w * 0.3f, h * 0.35f), w * 0.26f)
                    sparkle(color.copy(alpha = 0.7f), Offset(w * 0.75f, h * 0.25f), w * 0.16f)
                    sparkle(color.copy(alpha = 0.85f), Offset(w * 0.7f, h * 0.7f), w * 0.22f)
                    sparkle(color.copy(alpha = 0.5f), Offset(w * 0.2f, h * 0.8f), w * 0.1f)
                }
            }
        }
    }
}