
package com.example.bitwise_assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image

@Composable
fun GamesScreen(
    onTebakAngka: () -> Unit,
    onTicTacToe: () -> Unit,
    onFoxAdventure: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {


        // Judul dan maskot
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.58f)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Games",
                    color = TextDark,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Main, belajar, dan nikmati keseruan bareng Bitwise!",
                    color = TextGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            FoxMascot(
                mood = FoxMood.HAPPY,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        GameMenuCard(
            number = "01",
            title = "Tebak Angka",
            description = "Coba tebak angka 1-100 dalam 5 kesempatan!",
            onClick = onTebakAngka
        )

        Spacer(modifier = Modifier.height(14.dp))

        GameMenuCard(
            number = "02",
            title = "Tic Tac Toe",
            description = "Main bareng teman atau lawan AI!",
            onClick = onTicTacToe
        )

        Spacer(modifier = Modifier.height(14.dp))

        GameMenuCard(
            number = "03",
            title = "Fox Adventure",
            description = "Terbang, kumpulkan koin, hindari rintangan!",
            onClick = onFoxAdventure
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun GameMenuCard(
    number: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBgColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            BorderColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = number,
                color = Purple,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = TextDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                color = TextGray,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Mainkan",
                color = Purple,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
