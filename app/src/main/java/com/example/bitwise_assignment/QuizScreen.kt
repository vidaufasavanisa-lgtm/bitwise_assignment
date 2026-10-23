package com.example.bitwise_assignment

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =====================================================================
// DATA & PENYIMPANAN LOKAL (variabel, belum pakai database)
// =====================================================================

enum class QuestionType { SINGLE, MULTI }

data class QuizQuestion(
    val type: QuestionType,
    val text: String,
    val options: List<String>,
    val correct: Set<Int>          // index jawaban yang benar
)

/** 1 segmen = 1 materi (isinya campuran pilihan tunggal & pilihan ganda) */
data class QuizSegment(
    val id: Int,
    val title: String,
    val description: String,
    val emoji: String,
    val questions: List<QuizQuestion>
)

/** Satu jawaban dari peserta (dipakai admin untuk statistik) */
data class QuizAnswer(
    val segmentId: Int,
    val questionIndex: Int,
    val selected: Set<Int>,
    val isCorrect: Boolean
)

object QuizStore {


    val segments = listOf(
        // ---------------- SEGMEN 1: MATEMATIKA ----------------
        QuizSegment(
            id = 1,
            title = "Matematika",
            description = "Hitungan dasar, bilangan, dan geometri.",
            emoji = "📐",
            questions = listOf(
                QuizQuestion(QuestionType.SINGLE, "Hasil dari 12 × 8 adalah ...",
                    listOf("86", "96", "108", "98", "88"), setOf(1)),
                QuizQuestion(QuestionType.MULTI, "Manakah yang termasuk bilangan prima?",
                    listOf("2", "4", "7", "9", "11"), setOf(0, 2, 4)),
                QuizQuestion(QuestionType.SINGLE, "Luas persegi dengan panjang sisi 7 cm adalah ...",
                    listOf("14 cm²", "28 cm²", "49 cm²", "56 cm²", "21 cm²"), setOf(2)),
                QuizQuestion(QuestionType.MULTI, "Manakah yang termasuk bilangan genap?",
                    listOf("12", "15", "18", "21", "24"), setOf(0, 2, 4)),
                QuizQuestion(QuestionType.SINGLE, "Nilai dari 3² + 4² adalah ...",
                    listOf("49", "7", "12", "25", "14"), setOf(3))
            )
        ),
        // ---------------- SEGMEN 2: INFORMATIKA ----------------
        QuizSegment(
            id = 2,
            title = "Informatika",
            description = "Teknologi, komputer, dan pemrograman.",
            emoji = "💻",
            questions = listOf(
                QuizQuestion(QuestionType.SINGLE, "Bahasa pemrograman utama untuk Android modern adalah ...",
                    listOf("Swift", "Ruby", "PHP", "Kotlin", "Perl"), setOf(3)),
                QuizQuestion(QuestionType.MULTI, "Manakah dari berikut yang merupakan alat komunikasi digital?",
                    listOf("Televisi", "Email", "Surat", "Radio", "Smartphone"), setOf(1, 4)),
                QuizQuestion(QuestionType.SINGLE, "Kepanjangan dari CPU adalah ...",
                    listOf("Central Process Unit", "Central Processing Unit", "Computer Personal Unit",
                        "Control Processing Unit", "Central Program Unit"), setOf(1)),
                QuizQuestion(QuestionType.MULTI, "Manakah yang termasuk perangkat input komputer?",
                    listOf("Mouse", "Monitor", "Printer", "Keyboard", "Scanner"), setOf(0, 3, 4)),
                QuizQuestion(QuestionType.SINGLE, "Manakah yang merupakan format file gambar?",
                    listOf(".mp3", ".docx", ".exe", ".zip", ".png"), setOf(4))
            )
        )
    )

    fun segment(id: Int): QuizSegment = segments.firstOrNull { it.id == id } ?: segments.first()

    /** Semua jawaban peserta */
    val answers = mutableStateListOf<QuizAnswer>()

    fun clearAll() = answers.clear()
}

private val QuizGreen = Color(0xFF16A34A)
private val QuizRed = Color(0xFFDC2626)
private val ChartColors = listOf(
    Color(0xFFC4B5FD), // A — Lavender
    Color(0xFF60A5FA), // B — Blue
    Color(0xFF8B5CF6), // C — Violet
    Color(0xFFEC4899), // D — Pink
    Color(0xFF5B21B6)  // E — Deep Purple
)

// =====================================================================
// KOMPONEN UMUM
// =====================================================================

@Composable
private fun ScreenScaffold(
    onBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        content()
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun TopBar(title: String, counter: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Purple)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(counter, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
private fun BigCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun PrimaryButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple,
            disabledContainerColor = Purple.copy(alpha = 0.4f)
        )
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}



@Composable
private fun OptionRow(
    letter: Char,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    val optBg = if (selected) Purple.copy(alpha = 0.12f) else CardBgColor
    val optBorder = if (selected) Purple else BorderColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(optBg)
            .border(1.dp, optBorder, shape)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$letter.  $text",
            color = if (selected) Purple else TextDark,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ResultBox(label: String, value: Int, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$value", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(label, fontSize = 13.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

/** Pie / donut chart sederhana */
@Composable
fun PieChart(
    values: List<Int>,
    colors: List<Color>,
    modifier: Modifier = Modifier.size(120.dp),
    centerTop: String? = null,
    centerBottom: String? = null
) {
    val total = values.sum()
    val trackColor = BorderColor
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = size.minDimension * 0.14f
            val diameter = size.minDimension - strokeW
            val topLeft = Offset(strokeW / 2f, strokeW / 2f)
            val arcSize = Size(diameter, diameter)
            if (total == 0) {
                drawArc(trackColor, 0f, 360f, false, topLeft, arcSize, style = Stroke(strokeW))
            } else {
                var start = -90f
                values.forEachIndexed { i, v ->
                    if (v > 0) {
                        val sweep = 360f * v / total
                        drawArc(
                            colors[i % colors.size], start, sweep, false, topLeft, arcSize,
                            style = Stroke(strokeW, cap = StrokeCap.Butt)
                        )
                        start += sweep
                    }
                }
            }
        }
        if (centerTop != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(centerTop, fontSize = 11.sp, color = MutedText)
                if (centerBottom != null)
                    Text(centerBottom, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
        }
    }
}

@Composable
fun QuizMenuScreen(
    onSegmentClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(onBack = onBack) {
        Text("Quick Quiz!", color = Purple, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text("Uji pengetahuanmu lewat pertanyaan", color = TextGray, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))

        QuizStore.segments.forEach { seg ->
            SegmentCard(
                segmentId = seg.id,
                title = seg.title,
                info = "${seg.questions.size} soal",
                onClick = { onSegmentClick(seg.id) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun QuizSegmentIllustration(segmentId: Int, modifier: Modifier = Modifier, iconSize: Dp = 28.dp) {
    val color = Purple
    Canvas(modifier = modifier.size(iconSize)) {
        val w = size.width
        val h = size.height
        val strokeW = 2.5.dp.toPx()

        if (segmentId == 1) {
            val path = Path().apply {
                moveTo(w * 0.5f, h * 0.15f)
                lineTo(w * 0.88f, h * 0.85f)
                lineTo(w * 0.12f, h * 0.85f)
                close()
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeW))

            drawLine(color, Offset(w * 0.5f, h * 0.45f), Offset(w * 0.5f, h * 0.65f), strokeWidth = strokeW, cap = StrokeCap.Round)
            drawLine(color, Offset(w * 0.4f, h * 0.55f), Offset(w * 0.6f, h * 0.55f), strokeWidth = strokeW, cap = StrokeCap.Round)
        } else if (segmentId == 2) {
            drawRoundRect(
                color = color,
                topLeft = Offset(w * 0.12f, h * 0.15f),
                size = Size(w * 0.76f, h * 0.55f),
                cornerRadius = CornerRadius(3.dp.toPx()),
                style = Stroke(width = strokeW)
            )
            drawLine(color, Offset(w * 0.38f, h * 0.82f), Offset(w * 0.62f, h * 0.82f), strokeWidth = strokeW, cap = StrokeCap.Round)
            drawLine(color, Offset(w * 0.5f, h * 0.7f), Offset(w * 0.5f, h * 0.82f), strokeWidth = strokeW)

            val bracketLeft = Path().apply {
                moveTo(w * 0.38f, h * 0.32f)
                lineTo(w * 0.28f, h * 0.425f)
                lineTo(w * 0.38f, h * 0.53f)
            }
            drawPath(bracketLeft, color, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

            val bracketRight = Path().apply {
                moveTo(w * 0.62f, h * 0.32f)
                lineTo(w * 0.72f, h * 0.425f)
                lineTo(w * 0.62f, h * 0.53f)
            }
            drawPath(bracketRight, color, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        } else {
            drawRoundRect(
                color = color,
                topLeft = Offset(w * 0.18f, h * 0.12f),
                size = Size(w * 0.64f, h * 0.76f),
                cornerRadius = CornerRadius(3.dp.toPx()),
                style = Stroke(width = strokeW)
            )
            drawLine(color, Offset(w * 0.35f, h * 0.35f), Offset(w * 0.68f, h * 0.35f), strokeWidth = strokeW, cap = StrokeCap.Round)
            drawLine(color, Offset(w * 0.35f, h * 0.55f), Offset(w * 0.68f, h * 0.55f), strokeWidth = strokeW, cap = StrokeCap.Round)
        }
    }
}

@Composable
private fun ResultBadgeIllustration(score: Int, modifier: Modifier = Modifier) {
    val color = Purple
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Purple.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 3.dp.toPx()

            if (score >= 80) {
                val path = Path().apply {
                    val cx = w / 2f
                    val cy = h / 2f
                    val outerRadius = w * 0.45f
                    val innerRadius = w * 0.2f
                    val points = 5
                    for (i in 0 until points * 2) {
                        val r = if (i % 2 == 0) outerRadius else innerRadius
                        val angle = (i * Math.PI / points) - (Math.PI / 2)
                        val x = cx + (r * Math.cos(angle)).toFloat()
                        val y = cy + (r * Math.sin(angle)).toFloat()
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                drawPath(path, color = color, style = Stroke(width = strokeW, join = StrokeJoin.Round))
            } else if (score >= 50) {
                drawCircle(color, radius = w * 0.38f, style = Stroke(width = strokeW))
                val check = Path().apply {
                    moveTo(w * 0.32f, h * 0.5f)
                    lineTo(w * 0.45f, h * 0.62f)
                    lineTo(w * 0.68f, h * 0.38f)
                }
                drawPath(check, color, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
            } else {
                drawCircle(color, radius = w * 0.42f, style = Stroke(width = strokeW))
                drawCircle(color, radius = w * 0.22f, style = Stroke(width = strokeW))
                drawCircle(color, radius = w * 0.08f)
            }
        }
    }
}

@Composable
private fun SegmentCard(segmentId: Int, title: String, info: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(Purple.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                QuizSegmentIllustration(segmentId = segmentId, iconSize = 28.dp)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 16.sp)
                Spacer(Modifier.height(2.dp))
                Text(info, color = MutedText, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Purple),
                contentAlignment = Alignment.Center
            ) {
                Text("→", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// =====================================================================
// KUIS (1 segmen, soal campuran tunggal & ganda)
// =====================================================================

private enum class Phase { INTRO, ANSWER, FEEDBACK, RESULT }

private fun calculateQuestionScore(q: QuizQuestion, selected: Set<Int>): Float {
    if (q.type == QuestionType.SINGLE) {
        return if (selected == q.correct) 1.0f else 0.0f
    } else {
        val correctSet = q.correct
        val k = correctSet.size
        if (k == 0) return if (selected.isEmpty()) 1.0f else 0.0f
        
        val corrSelected = selected.intersect(correctSet).size
        if (corrSelected == 0) return 0.0f
        
        val fraction = corrSelected.toFloat() / k.toFloat()
        return minOf(1.0f, fraction)
    }
}

@Composable
fun QuizPlayScreen(segmentId: Int, onBack: () -> Unit) {
    val segment = QuizStore.segment(segmentId)
    val questions = segment.questions
    val title = "Segmen ${segment.id} · ${segment.title}"

    var index by remember(segmentId) { mutableIntStateOf(0) }
    var selected by remember(segmentId) { mutableStateOf(setOf<Int>()) }
    var phase by remember(segmentId) { mutableStateOf(Phase.INTRO) }
    var lastScoreFraction by remember(segmentId) { mutableFloatStateOf(0f) }
    val results = remember(segmentId) { mutableStateListOf<Float>() }
    val userAnswers = remember(segmentId) { mutableStateListOf<Set<Int>>() }

    ScreenScaffold(onBack = onBack) {

        // ====================== INTRO / MULAI KUIS ======================
        if (phase == Phase.INTRO) {
            TopBar(title, "Persiapan")
            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBgColor),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Purple.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        QuizSegmentIllustration(segmentId = segment.id, iconSize = 44.dp)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        segment.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        segment.description,
                        fontSize = 14.sp,
                        color = TextGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(Modifier.height(20.dp))

                    HorizontalDivider(color = BorderColor)
                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Jumlah Soal:", fontSize = 14.sp, color = TextGray)
                        Text("${questions.size} Pertanyaan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Tipe Soal:", fontSize = 14.sp, color = TextGray)
                        Text("Pilihan Tunggal & Ganda", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            PrimaryButton("Mulai Kuis") {
                phase = Phase.ANSWER
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Purple)
            ) {
                Text("Kembali", color = Purple, fontWeight = FontWeight.Bold)
            }
            return@ScreenScaffold
        }

        // ====================== HASIL AKHIR ======================
        if (phase == Phase.RESULT) {
            val totalScoreSum = results.sum()
            val total = questions.size
            val score = if (total == 0) 0 else Math.round((totalScoreSum / total) * 100f)

            val benarCount = results.count { it >= 0.99f }
            val partialCount = results.count { it > 0f && it < 0.99f }
            val salahCount = results.count { it == 0f }

            // Simple scale animation on appearance
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }
            val animScale by animateFloatAsState(
                targetValue = if (visible) 1f else 0.85f,
                animationSpec = tween(500),
                label = "resultScale"
            )

            TopBar(title, "Hasil Akhir")
            Spacer(Modifier.height(16.dp))

            // Hero Result Card (Quizizz / Kahoot style)
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CardBgColor),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(scaleX = animScale, scaleY = animScale)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Trophy / Badge Illustration
                    ResultBadgeIllustration(score = score)
                    Spacer(Modifier.height(16.dp))

                    // Achievement Message
                    Text(
                        text = when {
                            score >= 80 -> "Luar Biasa! Hebat Sekali!"
                            score >= 50 -> "Kerja Bagus! Pertahankan!"
                            else -> "Terus Berusaha, Kamu Pasti Bisa!"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))

                    // Big Score Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Purple.copy(alpha = 0.12f))
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SKOR AKHIR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Purple, letterSpacing = 1.5.sp)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "$score",
                                fontSize = 52.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Purple
                            )
                            Spacer(Modifier.height(2.dp))
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Stats Row: Benar, Parsial, Salah
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ResultBox("Benar", benarCount, QuizGreen, Modifier.weight(1f))
                        ResultBox("Parsial", partialCount, Color(0xFFD97706), Modifier.weight(1f))
                        ResultBox("Salah", salahCount, QuizRed, Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Rincian per soal & Jawaban Benar / Pilihan Pengguna
            Text("Rincian Soal & Jawaban", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
            Spacer(Modifier.height(12.dp))

            questions.forEachIndexed { qi, q ->
                val scoreFraction = results.getOrNull(qi) ?: 0f
                val isFull = scoreFraction >= 0.99f
                val isPart = scoreFraction > 0f && !isFull
                val userSelected = userAnswers.getOrNull(qi) ?: emptySet()
                
                val statusColor = when {
                    isFull -> QuizGreen
                    isPart -> Color(0xFFD97706)
                    else -> QuizRed
                }
                val statusText = if (q.type == QuestionType.SINGLE) {
                    if (isFull) "Benar" else "Salah"
                } else {
                    val corrSelected = userSelected.intersect(q.correct).size
                    val k = q.correct.size
                    if (isFull) "Benar ($k/$k)" else if (corrSelected > 0) "Sebagian $corrSelected/$k" else "Salah (0/$k)"
                }

                BigCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(statusColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    when {
                                        isFull -> "✓"
                                        isPart -> "½"
                                        else -> "✕"
                                    },
                                    color = statusColor, fontWeight = FontWeight.Bold, fontSize = 13.sp
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Soal ${qi + 1}",
                                fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = statusColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                statusText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(q.text, fontSize = 14.sp, color = TextGray)
                    Spacer(Modifier.height(14.dp))

                    Text("Opsi Jawaban:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MutedText)
                    Spacer(Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        q.options.forEachIndexed { oi, opt ->
                            val isCorrectOpt = oi in q.correct
                            val isUserChosen = oi in userSelected

                            val optBg = when {
                                isCorrectOpt -> QuizGreen.copy(alpha = 0.12f)
                                isUserChosen && !isCorrectOpt -> QuizRed.copy(alpha = 0.12f)
                                else -> CardBgColor
                            }
                            val optBorder = when {
                                isCorrectOpt -> QuizGreen
                                isUserChosen && !isCorrectOpt -> QuizRed
                                else -> BorderColor
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(optBg)
                                    .border(1.dp, optBorder, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${'A' + oi}.  $opt",
                                    fontSize = 14.sp,
                                    color = if (isCorrectOpt) QuizGreen else if (isUserChosen) QuizRed else TextDark,
                                    fontWeight = if (isCorrectOpt || isUserChosen) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(12.dp))
            PrimaryButton("Main Lagi") {
                index = 0; selected = emptySet(); results.clear(); userAnswers.clear(); phase = Phase.ANSWER
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Purple)
            ) { Text("Kembali ke Explore", color = Purple, fontWeight = FontWeight.Bold) }
            return@ScreenScaffold
        }

        // ====================== SOAL ======================
        val q = questions[index]
        val multi = q.type == QuestionType.MULTI
        TopBar(title, "${index + 1}/${questions.size}")
        Spacer(Modifier.height(12.dp))

        if (phase == Phase.ANSWER) {
            Text("${index + 1}.  ${q.text}", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextDark)
            Spacer(Modifier.height(4.dp))
            Text(
                if (multi) "Pilih satu atau lebih jawaban yang benar."
                else "Pilih satu jawaban yang benar.",
                fontSize = 12.sp, color = MutedText
            )
            Spacer(Modifier.height(14.dp))
            q.options.forEachIndexed { i, opt ->
                OptionRow(
                    letter = 'A' + i, text = opt,
                    selected = i in selected,
                    onClick = {
                        selected = if (multi) {
                            val maxAllowed = q.correct.size
                            if (i in selected) {
                                selected - i
                            } else {
                                if (selected.size < maxAllowed) selected + i else selected
                            }
                        } else {
                            setOf(i)
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(6.dp))
            PrimaryButton("Jawab", enabled = selected.isNotEmpty()) {
                val scoreFraction = calculateQuestionScore(q, selected)
                lastScoreFraction = scoreFraction
                results.add(scoreFraction)
                userAnswers.add(selected)
                QuizStore.answers.add(QuizAnswer(segment.id, index, selected, scoreFraction > 0f))
                phase = Phase.FEEDBACK
            }
        } else {
            // ---------- FEEDBACK ----------
            val isFull = lastScoreFraction >= 0.99f
            val isPart = lastScoreFraction > 0f && !isFull
            val corrSelected = selected.intersect(q.correct).size
            val k = q.correct.size

            val statusColor = when {
                isFull -> QuizGreen
                isPart -> Color(0xFFD97706)
                else -> QuizRed
            }

            val statusText = if (q.type == QuestionType.SINGLE) {
                if (isFull) "Benar" else "Salah"
            } else {
                if (isFull) "Benar ($k/$k)" else if (corrSelected > 0) "$corrSelected/$k" else "Salah (0/$k)"
            }

            val statusIcon = when {
                isFull -> "✓"
                isPart -> "½"
                else -> "✕"
            }

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(statusColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        statusIcon,
                        color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    statusText,
                    fontSize = 22.sp, fontWeight = FontWeight.ExtraBold,
                    color = statusColor
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Pilihan jawaban yang benar adalah:",
                    fontSize = 13.sp, color = TextGray, textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Purple.copy(alpha = 0.08f))
                    .padding(14.dp)
            ) {
                Spacer(Modifier.height(4.dp))
                q.correct.sorted().forEach { i ->
                    Text("${'A' + i}.  ${q.options[i]}", fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(16.dp))
            val isLast = index == questions.lastIndex
            PrimaryButton(if (isLast) "Lihat Hasil" else "Lanjut Soal") {
                if (isLast) phase = Phase.RESULT
                else {
                    index++; selected = emptySet(); phase = Phase.ANSWER
                }
            }
        }
    }
}

// =====================================================================
// ADMIN: statistik kuis (pie chart) - TIDAK terlihat oleh peserta
// Dibuka dari menu Settings dan dilindungi PIN.
// =====================================================================

@Composable
fun QuizAdminScreen(onBack: () -> Unit) {
    var mainTab by remember { mutableIntStateOf(0) }
    var quizSegmentTab by remember { mutableIntStateOf(0) }

    ScreenScaffold(onBack = onBack) {
        Text("Statistik", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
        Spacer(Modifier.height(4.dp))
        // Main Tab: Kuis vs Kuesioner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Purple.copy(alpha = 0.12f))
                .padding(4.dp)
        ) {
            listOf("Kuis", "Kuesioner").forEachIndexed { i, title ->
                val sel = mainTab == i
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (sel) Purple else Color.Transparent)
                        .clickable { mainTab = i }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (sel) Color.White else Purple
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))

        if (mainTab == 0) {
            // Tab per segmen Kuis
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                QuizStore.segments.forEachIndexed { i, seg ->
                    val sel = quizSegmentTab == i
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (sel) Purple else Purple.copy(alpha = 0.12f))
                            .clickable { quizSegmentTab = i }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Segmen ${seg.id}", fontWeight = FontWeight.Bold, fontSize = 13.sp,
                            color = if (sel) Color.White else Purple
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            AdminSegmentStats(QuizStore.segments[quizSegmentTab])

            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = { QuizStore.clearAll() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, QuizRed)
            ) { Text("Hapus Data Kuis", color = QuizRed, fontWeight = FontWeight.Bold) }
        } else {
            AdminQuestionnaireStats()

            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = { QuestionnaireStore.clearAll() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, QuizRed)
            ) { Text("Hapus Data Kuesioner", color = QuizRed, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun AdminQuestionnaireStats() {
    val answers = QuestionnaireStore.answers
    val questions = QuestionnaireStore.questions
    val totalResponden = if (questions.isEmpty()) 0 else answers.filter { it.questionIndex == 0 }.size

    Text("📊 Kuesioner Kebiasaan", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
    Spacer(Modifier.height(10.dp))

    BigCard {
        Text("Ringkasan Kuesioner", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ResultBox("Pertanyaan", questions.size, Purple, Modifier.weight(1f))
            ResultBox("Responden", totalResponden, QuizGreen, Modifier.weight(1f))
        }
    }
    Spacer(Modifier.height(12.dp))

    questions.forEachIndexed { qi, q ->
        val list = answers.filter { it.questionIndex == qi }
        val counts = q.options.indices.map { oi -> list.count { it.optionIndex == oi } }

        BigCard {
            Text("Pertanyaan ${qi + 1}", fontSize = 11.sp, color = Purple, fontWeight = FontWeight.Bold)
            Text(q.text, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
            Spacer(Modifier.height(14.dp))
            Text("Jumlah pemilih per opsi", fontSize = 12.sp, color = MutedText)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PieChart(
                    values = counts, colors = ChartColors,
                    modifier = Modifier.size(110.dp),
                    centerTop = "${list.size}", centerBottom = "jawaban"
                )
                Spacer(Modifier.width(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    q.options.forEachIndexed { oi, opt ->
                        val count = counts[oi]
                        val pct = if (list.isEmpty()) 0 else Math.round((count.toFloat() / list.size) * 100f)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(10.dp).clip(CircleShape).background(ChartColors[oi % ChartColors.size]))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "$opt : $count ($pct%)",
                                fontSize = 12.sp, color = TextDark,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun AdminSegmentStats(segment: QuizSegment) {
    val all = QuizStore.answers.filter { it.segmentId == segment.id }
    
    var segmentBenar = 0
    var segmentSebagian = 0
    var segmentSalah = 0
    
    all.forEach { ans ->
        val q = segment.questions.getOrNull(ans.questionIndex)
        if (q != null) {
            val sf = calculateQuestionScore(q, ans.selected)
            when {
                sf >= 0.99f -> segmentBenar++
                sf > 0f -> segmentSebagian++
                else -> segmentSalah++
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        QuizSegmentIllustration(segmentId = segment.id, iconSize = 22.dp)
        Spacer(Modifier.width(8.dp))
        Text(segment.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
    }
    Text("Total jawaban masuk: ${all.size}", fontSize = 13.sp, color = MutedText)
    Spacer(Modifier.height(10.dp))

    // Ringkasan angka benar/sebagian/salah
    BigCard {
        Text("Ringkasan Segmen", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ResultBox("Benar", segmentBenar, QuizGreen, Modifier.weight(1f))
            ResultBox("Sebagian", segmentSebagian, Color(0xFFD97706), Modifier.weight(1f))
            ResultBox("Salah", segmentSalah, QuizRed, Modifier.weight(1f))
        }
    }
    Spacer(Modifier.height(12.dp))

    // Detail per soal
    segment.questions.forEachIndexed { qi, q ->
        val list = all.filter { it.questionIndex == qi }
        var benar = 0
        var sebagian = 0
        var salah = 0
        list.forEach { ans ->
            val sf = calculateQuestionScore(q, ans.selected)
            when {
                sf >= 0.99f -> benar++
                sf > 0f -> sebagian++
                else -> salah++
            }
        }
        val counts = q.options.indices.map { oi -> list.count { oi in it.selected } }

        BigCard {
            Text(
                if (q.type == QuestionType.MULTI) "Pilihan Ganda" else "Pilihan Tunggal",
                fontSize = 11.sp, color = Purple, fontWeight = FontWeight.Bold
            )
            Text("${qi + 1}.  ${q.text}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ResultBox("Benar", benar, QuizGreen, Modifier.weight(1f))
                if (q.type == QuestionType.MULTI) {
                    ResultBox("Sebagian", sebagian, Color(0xFFD97706), Modifier.weight(1f))
                }
                ResultBox("Salah", salah, QuizRed, Modifier.weight(1f))
            }
            Spacer(Modifier.height(14.dp))
            Text("Jumlah pemilih per opsi", fontSize = 12.sp, color = MutedText)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PieChart(
                    values = counts, colors = ChartColors,
                    modifier = Modifier.size(110.dp),
                    centerTop = "${list.size}", centerBottom = "orang"
                )
                Spacer(Modifier.width(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    q.options.forEachIndexed { oi, opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(10.dp).clip(CircleShape).background(ChartColors[oi % ChartColors.size]))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "${'A' + oi}. $opt${if (oi in q.correct) " ✓" else ""} : ${counts[oi]}",
                                fontSize = 12.sp, color = TextDark,
                                fontWeight = if (oi in q.correct) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}