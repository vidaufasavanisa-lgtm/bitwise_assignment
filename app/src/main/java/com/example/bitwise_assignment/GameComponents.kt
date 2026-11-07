package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =====================================================================
// PALET WARNA GAMES (dark purple & lavender, sesuai desain)
// =====================================================================
val GameBg = Color(0xFF161033)
val GameCard = Color(0xFF2B2260)
val GameCardDeep = Color(0xFF1F1847)
val GameField = Color(0xFF3B3177)
val GameBorder = Color(0xFF6C54C8)
val GameLavender = Color(0xFFB79CF5)
val GameLavenderSoft = Color(0xFFD9CCFB)
val GameDarkText = Color(0xFF1A1433)
val GameMuted = Color(0xFFA79BD0)
val GamePink = Color(0xFFF472B6)
val GameGold = Color(0xFFFFC83D)

// =====================================================================
// MASKOT FOX
// =====================================================================
enum class FoxMood { NORMAL, HAPPY, THINKING, SAD, JETPACK }

@Composable
fun FoxMascot(mood: FoxMood, modifier: Modifier = Modifier) {
    val res = when (mood) {
        FoxMood.NORMAL -> R.drawable.fox_normal
        FoxMood.HAPPY -> R.drawable.fox_happy
        FoxMood.THINKING -> R.drawable.fox_thinking
        FoxMood.SAD -> R.drawable.fox_sad
        FoxMood.JETPACK -> R.drawable.fox_jetpack
    }
    Image(
        painter = painterResource(id = res),
        contentDescription = "Maskot Bitwise Fox",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

// =====================================================================
// KOMPONEN UMUM
// =====================================================================
@Composable
fun GameScreenScaffold(
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GameBg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun GameBackArrow(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
    }
}

/** Baris judul: ikon + teks di tengah, panah back di kiri atas */
@Composable
fun GameTitleHeader(title: String, icon: ImageVector, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        GameBackArrow(onBack)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = GameLavender, modifier = Modifier.size(34.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = GameLavender, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun GamePrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GameLavender,
            contentColor = GameDarkText,
            disabledContainerColor = GameLavender.copy(alpha = 0.4f),
            disabledContentColor = GameDarkText.copy(alpha = 0.6f)
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun GameSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GameBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = GameCardDeep,
            contentColor = Color.White
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

// =====================================================================
// LAYAR HASIL (dipakai semua game)
// Menang  -> fox senang
// Kalah   -> fox sedih ("Game Over")
// Seri    -> fox berpikir
// =====================================================================
@Composable
fun GameResultScreen(
    mood: FoxMood,
    title: String,
    message: String,
    scoreValue: Int? = null,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GameBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FoxMascot(mood, modifier = Modifier.height(220.dp))
        Spacer(Modifier.height(20.dp))
        Text(
            title,
            color = GameLavender,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            message,
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )

        if (scoreValue != null) {
            Spacer(Modifier.height(22.dp))
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GameCard),
                border = BorderStroke(1.dp, GameBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = GameGold,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Skor Akhir", color = GameMuted, fontSize = 12.sp)
                        Text(
                            "$scoreValue",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))
        GamePrimaryButton("Main Lagi", icon = Icons.Default.Refresh, onClick = onPlayAgain)
        Spacer(Modifier.height(12.dp))
        GameSecondaryButton("Kembali ke Menu", icon = Icons.Default.Home, onClick = onMenu)
    }
}

