
package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private const val MAX_ATTEMPTS = 5

private enum class GuessState {
    PLAYING, WON, LOST
}

@Composable
fun TebakAngkaScreen(onBack: () -> Unit) {

    var target by remember {
        mutableIntStateOf(Random.nextInt(1, 101))
    }

    var attempts by remember {
        mutableIntStateOf(MAX_ATTEMPTS)
    }

    var input by remember {
        mutableStateOf("")
    }

    var hint by remember {
        mutableStateOf<String?>(null)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var state by remember {
        mutableStateOf(GuessState.PLAYING)
    }

    fun newRound() {
        target = Random.nextInt(1, 101)
        attempts = MAX_ATTEMPTS
        input = ""
        hint = null
        state = GuessState.PLAYING
    }

    fun check() {
        val guess = input.toIntOrNull()

        if (guess == null || guess !in 1..100) {
            hint = "Masukkan angka antara 1 sampai 100 ya!"
            return
        }

        if (guess == target) {
            score += attempts * 20
            state = GuessState.WON
        } else {
            attempts--

            hint = if (guess < target) {
                "$guess terlalu kecil! Coba angka yang lebih besar."
            } else {
                "$guess terlalu besar! Coba angka yang lebih kecil."
            }

            if (attempts <= 0) {
                state = GuessState.LOST
            }
        }

        input = ""
    }

    when (state) {

        GuessState.WON -> GameResultScreen(
            mood = FoxMood.HAPPY,
            title = "Kamu Menang!",
            message = "Hebat! Kamu berhasil menebak angka $target dengan benar!",
            scoreValue = score,
            onPlayAgain = { newRound() },
            onMenu = onBack
        )

        GuessState.LOST -> GameResultScreen(
            mood = FoxMood.SAD,
            title = "Game Over",
            message = "Kesempatanmu habis. Angka yang benar adalah $target. Jangan menyerah!",
            scoreValue = null,
            onPlayAgain = {
                score = 0
                newRound()
            },
            onMenu = onBack
        )

        GuessState.PLAYING -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgColor)
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Judul halaman tanpa tombol kembali
                Text(
                    text = "Tebak Angka",
                    color = TextDark,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Tebak angka antara 1 - 100\ndalam 5 kesempatan!",
                    color = TextGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Maskot fox
                FoxMascot(
                    mood = FoxMood.THINKING,
                    modifier = Modifier.height(190.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Kartu input angka
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBgColor
                    ),
                    border = BorderStroke(
                        1.dp,
                        BorderColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        if (hint != null) {
                            Text(
                                text = hint!!,
                                color = Purple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )
                        }

                        TextField(
                            value = input,
                            onValueChange = { value ->
                                if (
                                    value.length <= 3 &&
                                    value.all { it.isDigit() }
                                ) {
                                    input = value
                                }
                            },
                            placeholder = {
                                Text(
                                    text = "Masukkan angka (1 - 100)",
                                    color = MutedText
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { check() }
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = if (LocalDarkTheme.current)
                                    Color(0xFF2A1D35)
                                else
                                    Color(0xFFF7F6FC),

                                unfocusedContainerColor = if (LocalDarkTheme.current)
                                    Color(0xFF2A1D35)
                                else
                                    Color(0xFFF7F6FC),

                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark,
                                cursorColor = Purple,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GamePrimaryButton(
                            text = "Cek",
                            enabled = input.isNotEmpty(),
                            onClick = { check() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Informasi kesempatan dan skor
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBgColor
                    ),
                    border = BorderStroke(
                        1.dp,
                        BorderColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Indikator kesempatan tanpa ikon
                        Column(
                            modifier = Modifier.weight(1.6f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Kesempatan",
                                color = TextGray,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                repeat(MAX_ATTEMPTS) { index ->
                                    Box(
                                        modifier = Modifier
                                            .width(18.dp)
                                            .height(8.dp)
                                            .background(
                                                color = if (index < attempts) {
                                                    Purple
                                                } else {
                                                    BorderColor
                                                },
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "$attempts dari $MAX_ATTEMPTS",
                                color = MutedText,
                                fontSize = 11.sp
                            )
                        }

                        // Garis pemisah
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(48.dp)
                                .background(BorderColor)
                        )

                        // Skor
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Skor",
                                color = TextGray,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "$score",
                                color = Purple,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
