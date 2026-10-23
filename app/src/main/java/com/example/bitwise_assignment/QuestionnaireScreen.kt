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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class QuestionnaireQuestion(
    val id: Int,
    val text: String,
    val options: List<String>
)

data class QuestionnaireAnswer(
    val questionIndex: Int,
    val optionIndex: Int
)

object QuestionnaireStore {
    val questions = listOf(
        QuestionnaireQuestion(
            id = 1,
            text = "Apa hobi yang paling kamu sukai?",
            options = listOf("Menggambar", "Bermain game", "Berolahraga", "Memasak", "Mendengarkan musik")
        ),
        QuestionnaireQuestion(
            id = 2,
            text = "Makanan apa yang paling kamu sukai?",
            options = listOf("Nasi goreng", "Mi ayam", "Sate", "Rendang", "Seblak")
        ),
        QuestionnaireQuestion(
            id = 3,
            text = "Kapan kamu paling suka belajar?",
            options = listOf("Pagi hari", "Siang hari", "Sore hari", "Malam hari", "Mendekati ujian")
        ),
        QuestionnaireQuestion(
            id = 4,
            text = "Apa yang biasanya kamu lakukan saat sedang bosan?",
            options = listOf("Menonton film", "Bermain game", "Tidur", "Keluar bersama teman", "Membaca atau mencari informasi")
        ),
        QuestionnaireQuestion(
            id = 5,
            text = "Minuman apa yang paling sering kamu pilih?",
            options = listOf("Air putih", "Teh", "Kopi", "Susu", "Jus buah")
        )
    )

    val answers = mutableStateListOf<QuestionnaireAnswer>()

    fun clearAll() = answers.clear()
}

@Composable
private fun QuestionnaireIntroIllustration(modifier: Modifier = Modifier) {
    val color = Purple
    Canvas(modifier = modifier.size(44.dp)) {
        val w = size.width
        val h = size.height
        val strokeW = 3.dp.toPx()
        val barWidth = w * 0.18f
        val cornerRadius = CornerRadius(3.dp.toPx())

        drawLine(color, Offset(w * 0.1f, h * 0.88f), Offset(w * 0.9f, h * 0.88f), strokeWidth = strokeW, cap = StrokeCap.Round)

        val h1 = h * 0.35f
        drawRoundRect(
            color = color.copy(alpha = 0.5f),
            topLeft = Offset(w * 0.16f, h * 0.88f - h1),
            size = Size(barWidth, h1),
            cornerRadius = cornerRadius
        )

        val h2 = h * 0.68f
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

@Composable
private fun QuestionnaireSuccessIllustration(modifier: Modifier = Modifier) {
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

            drawCircle(color, radius = w * 0.42f, style = Stroke(width = strokeW))
            val check = Path().apply {
                moveTo(w * 0.28f, h * 0.5f)
                lineTo(w * 0.44f, h * 0.64f)
                lineTo(w * 0.72f, h * 0.36f)
            }
            drawPath(check, color, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

private enum class QuestionnairePhase { INTRO, ANSWER, RESULT }

@Composable
fun QuestionnaireScreen(onBack: () -> Unit) {
    val questions = QuestionnaireStore.questions
    var index by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var phase by remember { mutableStateOf(QuestionnairePhase.INTRO) }
    val userAnswers = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ================= INTRO =================
        if (phase == QuestionnairePhase.INTRO) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Purple)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Kuesioner Kebiasaan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Persiapan", color = Color.White, fontSize = 13.sp)
            }
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
                        QuestionnaireIntroIllustration()
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Kuesioner Preferensi",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Isi kuesioner singkat ini untuk membagikan preferensi dan kebiasaan favoritmu.",
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
                        Text("Jumlah Pertanyaan:", fontSize = 14.sp, color = TextGray)
                        Text("${questions.size} Pertanyaan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { phase = QuestionnairePhase.ANSWER },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Mulai Kuesioner", color = Color.White, fontWeight = FontWeight.Bold)
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
            Spacer(Modifier.height(24.dp))
            return@Column
        }

        // ================= HASIL AKHIR =================
        if (phase == QuestionnairePhase.RESULT) {
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }
            val animScale by animateFloatAsState(
                targetValue = if (visible) 1f else 0.85f,
                animationSpec = tween(500),
                label = "kuesionerResultScale"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Purple)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Kuesioner Kebiasaan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Selesai", color = Color.White, fontSize = 13.sp)
            }
            Spacer(Modifier.height(16.dp))

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
                    QuestionnaireSuccessIllustration()
                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Terima kasih telah mengisi kuesioner!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Jawaban dan preferensi kamu telah berhasil direkam.",
                        fontSize = 14.sp,
                        color = TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Review Jawaban Anda", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
            Spacer(Modifier.height(12.dp))

            questions.forEachIndexed { qi, q ->
                val chosenIndex = userAnswers.getOrNull(qi)

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBgColor),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Pertanyaan ${qi + 1}",
                            fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(q.text, fontSize = 14.sp, color = TextGray)
                        Spacer(Modifier.height(14.dp))

                        Text("Pilihan Anda:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MutedText)
                        Spacer(Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            q.options.forEachIndexed { oi, opt ->
                                val isChosen = chosenIndex == oi
                                val optBg = if (isChosen) Purple.copy(alpha = 0.12f) else CardBgColor
                                val optBorder = if (isChosen) Purple else BorderColor

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
                                        opt,
                                        fontSize = 14.sp,
                                        color = if (isChosen) Purple else TextDark,
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    index = 0; selectedIndex = null; userAnswers.clear(); phase = QuestionnairePhase.ANSWER
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Isi Lagi", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Purple)
            ) { Text("Kembali ke Explore", color = Purple, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(24.dp))
            return@Column
        }

        // ================= PERTANYAAN =================
        val q = questions[index]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Purple)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kuesioner Kebiasaan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("${index + 1}/${questions.size}", color = Color.White, fontSize = 13.sp)
        }
        Spacer(Modifier.height(12.dp))

        Text("${index + 1}.  ${q.text}", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextDark)
        Spacer(Modifier.height(4.dp))
        Text("Pilih salah satu jawaban.", fontSize = 12.sp, color = MutedText)
        Spacer(Modifier.height(14.dp))

        q.options.forEachIndexed { i, opt ->
            val isSelected = selectedIndex == i
            val shape = RoundedCornerShape(10.dp)
            val optBg = if (isSelected) Purple.copy(alpha = 0.12f) else CardBgColor
            val optBorder = if (isSelected) Purple else BorderColor

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(optBg)
                    .border(1.dp, optBorder, shape)
                    .clickable { selectedIndex = i }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    opt,
                    color = if (isSelected) Purple else TextDark,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(12.dp))
        val isLast = index == questions.lastIndex
        Button(
            onClick = {
                selectedIndex?.let { chosen ->
                    userAnswers.add(chosen)
                    if (isLast) {
                        userAnswers.forEachIndexed { qIdx, optIdx ->
                            QuestionnaireStore.answers.add(QuestionnaireAnswer(qIdx, optIdx))
                        }
                        phase = QuestionnairePhase.RESULT
                    } else {
                        index++
                        selectedIndex = null
                    }
                }
            },
            enabled = selectedIndex != null,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                disabledContainerColor = Purple.copy(alpha = 0.4f)
            )
        ) {
            Text(if (isLast) "Selesai" else "Lanjut", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(24.dp))
    }
}
