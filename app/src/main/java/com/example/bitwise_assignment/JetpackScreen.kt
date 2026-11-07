
package com.example.bitwise_assignment.games

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bitwise_assignment.BgColor
import com.example.bitwise_assignment.CardBgColor
import com.example.bitwise_assignment.BorderColor
import com.example.bitwise_assignment.LocalDarkTheme
import com.example.bitwise_assignment.Purple
import com.example.bitwise_assignment.TextDark
import com.example.bitwise_assignment.TextGray
import com.example.bitwise_assignment.FoxMascot
import com.example.bitwise_assignment.FoxMood
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

private data class JetpackObstacle(
    val x: Float,
    val gapCenter: Float,
    val gapSize: Float,
    val passed: Boolean = false
)

private data class JetpackCoin(
    val x: Float,
    val y: Float,
    val collected: Boolean = false
)

@Composable
fun JetpackScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences("jetpack_game", Context.MODE_PRIVATE)
    }

    var bestScore by remember {
        mutableIntStateOf(preferences.getInt("best_score", 0))
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var playerY by remember {
        mutableFloatStateOf(0.5f)
    }

    var isFlying by remember {
        mutableStateOf(false)
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    var gameOver by remember {
        mutableStateOf(false)
    }

    val obstacles = remember {
        mutableStateListOf<JetpackObstacle>()
    }

    val coins = remember {
        mutableStateListOf<JetpackCoin>()
    }

    val isDark = LocalDarkTheme.current

    val skyTop = if (isDark) {
        Color(0xFF21132C)
    } else {
        Color(0xFFB9DDFB)
    }

    val skyBottom = if (isDark) {
        Color(0xFF352044)
    } else {
        Color(0xFFEAF5FF)
    }

    val obstacleColor = if (isDark) {
        Color(0xFF83C98B)
    } else {
        Color(0xFF3C9B62)
    }

    val groundColor = if (isDark) {
        Color(0xFF493052)
    } else {
        Color(0xFFB8D6B0)
    }

    fun startGame() {
        score = 0
        playerY = 0.5f
        isFlying = false
        gameOver = false

        obstacles.clear()
        coins.clear()

        obstacles.add(
            JetpackObstacle(
                x = 1.1f,
                gapCenter = 0.48f,
                gapSize = 0.34f
            )
        )

        isPlaying = true
    }

    fun endGame() {
        isPlaying = false
        isFlying = false
        gameOver = true

        if (score > bestScore) {
            bestScore = score
            preferences.edit()
                .putInt("best_score", bestScore)
                .apply()
        }
    }

    // Game loop: gravitasi, gerakan rintangan, koin, dan collision
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            var velocity = 0f
            var spawnTimer = 0f
            var coinTimer = 0f

            val gravity = 1.15f
            val liftPower = 2.3f
            val obstacleSpeed = 0.38f

            while (isActive) {
                delay(16L)

                val deltaTime = 0.016f

                // Gerakan karakter
                if (isFlying) {
                    velocity -= liftPower * deltaTime
                }

                velocity += gravity * deltaTime
                velocity = velocity.coerceIn(-0.85f, 0.85f)

                playerY += velocity * deltaTime
                playerY = playerY.coerceIn(-0.1f, 1.1f)

                // Membuat rintangan baru
                spawnTimer += deltaTime

                if (spawnTimer >= 1.8f) {
                    spawnTimer = 0f

                    val newGapCenter = Random.nextFloat()
                        .coerceIn(0.30f, 0.70f)

                    obstacles.add(
                        JetpackObstacle(
                            x = 1.1f,
                            gapCenter = newGapCenter,
                            gapSize = 0.34f
                        )
                    )
                }

                // Menggerakkan rintangan
                for (i in obstacles.indices) {
                    val obstacle = obstacles[i]
                    val newX = obstacle.x - obstacleSpeed * deltaTime

                    var newPassed = obstacle.passed

                    // Skor bertambah ketika berhasil melewati rintangan
                    if (!obstacle.passed && newX < 0.15f) {
                        score++
                        newPassed = true
                    }

                    obstacles[i] = obstacle.copy(
                        x = newX,
                        passed = newPassed
                    )
                }

                // Hapus rintangan yang sudah keluar layar
                obstacles.removeAll {
                    it.x < -0.15f
                }

                // Membuat koin secara berkala
                coinTimer += deltaTime

                if (coinTimer >= 2.3f) {
                    coinTimer = 0f

                    coins.add(
                        JetpackCoin(
                            x = 1.05f,
                            y = Random.nextFloat().coerceIn(0.20f, 0.80f)
                        )
                    )
                }

                // Menggerakkan koin
                for (i in coins.indices) {
                    val coin = coins[i]

                    coins[i] = coin.copy(
                        x = coin.x - obstacleSpeed * deltaTime
                    )
                }

                // Deteksi pengambilan koin
                for (i in coins.indices) {
                    val coin = coins[i]

                    if (
                        !coin.collected &&
                        kotlin.math.abs(coin.x - 0.18f) < 0.07f &&
                        kotlin.math.abs(coin.y - playerY) < 0.09f
                    ) {
                        coins[i] = coin.copy(collected = true)
                        score += 5
                    }
                }

                coins.removeAll {
                    it.x < -0.1f || it.collected
                }

                // Deteksi tabrakan dengan rintangan
                val playerX = 0.18f
                val playerHalfHeight = 0.035f
                var collided = false

                for (obstacle in obstacles) {
                    val isNearPlayer =
                        kotlin.math.abs(obstacle.x - playerX) < 0.065f

                    if (isNearPlayer) {
                        val gapTop =
                            obstacle.gapCenter - obstacle.gapSize / 2f

                        val gapBottom =
                            obstacle.gapCenter + obstacle.gapSize / 2f

                        val playerTop =
                            playerY - playerHalfHeight

                        val playerBottom =
                            playerY + playerHalfHeight

                        if (
                            playerTop < gapTop ||
                            playerBottom > gapBottom
                        ) {
                            collided = true
                            break
                        }
                    }
                }

                // Game berakhir jika menabrak atau keluar area
                if (
                    collided ||
                    playerY < 0.035f ||
                    playerY > 0.94f
                ) {
                    endGame()
                    break
                }
            }
        }
    }

    // Halaman permainan
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        if (!isPlaying) {
            // Halaman awal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FoxMascot(
                    mood = FoxMood.JETPACK,
                    modifier = Modifier.size(170.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Fox Jetpack Adventure",
                    color = TextDark,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Terbang setinggi mungkin dan hindari semua rintangan!",
                    color = TextGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBgColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        BorderColor
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cara Bermain",
                            color = Purple,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "• Tekan dan tahan layar untuk terbang ke atas.\n" +
                                    "• Lepaskan layar agar karakter turun.\n" +
                                    "• Hindari rintangan yang datang.\n" +
                                    "• Kumpulkan koin untuk mendapatkan bonus skor.",
                            color = TextGray,
                            fontSize = 13.sp,
                            lineHeight = 21.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Best Score: $bestScore",
                    color = Purple,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { startGame() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "PLAY GAME",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            // Area permainan
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(skyTop, skyBottom)
                        )
                    )
                    .pointerInput(isPlaying) {
                        detectTapGestures(
                            onPress = {
                                isFlying = true
                                try {
                                    awaitRelease()
                                } finally {
                                    isFlying = false
                                }
                            }
                        )
                    }
            ) {
                // Menggambar rintangan dan koin
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Awan dekorasi
                    drawCircle(
                        color = Color.White.copy(alpha = 0.35f),
                        radius = 35f,
                        center = Offset(
                            canvasWidth * 0.70f,
                            canvasHeight * 0.18f
                        )
                    )

                    drawCircle(
                        color = Color.White.copy(alpha = 0.35f),
                        radius = 25f,
                        center = Offset(
                            canvasWidth * 0.76f,
                            canvasHeight * 0.18f
                        )
                    )

                    // Rintangan
                    obstacles.forEach { obstacle ->
                        val obstacleWidth = canvasWidth * 0.085f
                        val x = obstacle.x * canvasWidth
                        val gapTop =
                            (obstacle.gapCenter - obstacle.gapSize / 2f) *
                                    canvasHeight
                        val gapBottom =
                            (obstacle.gapCenter + obstacle.gapSize / 2f) *
                                    canvasHeight

                        // Rintangan atas
                        drawRoundRect(
                            color = obstacleColor,
                            topLeft = Offset(
                                x - obstacleWidth / 2f,
                                0f
                            ),
                            size = Size(
                                obstacleWidth,
                                gapTop
                            ),
                            cornerRadius = CornerRadius(12f)
                        )

                        // Rintangan bawah
                        drawRoundRect(
                            color = obstacleColor,
                            topLeft = Offset(
                                x - obstacleWidth / 2f,
                                gapBottom
                            ),
                            size = Size(
                                obstacleWidth,
                                canvasHeight - gapBottom
                            ),
                            cornerRadius = CornerRadius(12f)
                        )
                    }

                    // Koin
                    coins.forEach { coin ->
                        drawCircle(
                            color = Color(0xFFFFC83D),
                            radius = 12.dp.toPx(),
                            center = Offset(
                                coin.x * canvasWidth,
                                coin.y * canvasHeight
                            )
                        )

                        drawCircle(
                            color = Color(0xFFFFE69A),
                            radius = 5.dp.toPx(),
                            center = Offset(
                                coin.x * canvasWidth,
                                coin.y * canvasHeight
                            )
                        )
                    }

                    // Tanah bagian bawah
                    drawRect(
                        color = groundColor,
                        topLeft = Offset(
                            0f,
                            canvasHeight * 0.97f
                        ),
                        size = Size(
                            canvasWidth,
                            canvasHeight * 0.03f
                        )
                    )
                }

                // Maskot Fox sebagai karakter pemain
                FoxMascot(
                    mood = FoxMood.JETPACK,
                    modifier = Modifier
                        .offset(
                            x = screenWidth * 0.12f,
                            y = screenHeight * playerY - 28.dp
                        )
                        .size(58.dp)
                )

                // Informasi skor
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SCORE",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = score.toString(),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Petunjuk kontrol
                Text(
                    text = "TEKAN & TAHAN UNTUK TERBANG",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp),
                    color = if (isDark) Color.White else Color(0xFF40536B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Popup Game Over
    if (gameOver) {
        Dialog(
            onDismissRequest = {
                // Popup ditutup melalui tombol
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBgColor
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    BorderColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FoxMascot(
                        mood = FoxMood.SAD,
                        modifier = Modifier.size(130.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Game Over!",
                        color = Purple,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Yah, Fox menabrak rintangan!",
                        color = TextGray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SCORE",
                                color = TextGray,
                                fontSize = 12.sp
                            )

                            Text(
                                text = score.toString(),
                                color = Purple,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "BEST SCORE",
                                color = TextGray,
                                fontSize = 12.sp
                            )

                            Text(
                                text = bestScore.toString(),
                                color = Purple,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            startGame()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Main Lagi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            gameOver = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardBgColor,
                            contentColor = TextDark
                        )
                    ) {
                        Text(
                            text = "Kembali ke Menu",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
