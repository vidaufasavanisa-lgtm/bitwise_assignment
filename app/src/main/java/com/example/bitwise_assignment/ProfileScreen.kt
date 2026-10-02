package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import android.view.ViewGroup
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ProfileScreen(memberId: Int, onBack: () -> Unit) {
    val member = MemberData.findById(memberId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AppHeader()
        Spacer(Modifier.height(24.dp))

        // Tombol kembali
        Text(
            "← Kembali",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = TextDark,
            modifier = Modifier.clickable { onBack() }
        )
        Spacer(Modifier.height(20.dp))

        if (member == null) {
            Text("Anggota tidak ditemukan", color = TextGray)
            return@Column
        }

        // Avatar + nama + role
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = member.photoResId),
                contentDescription = "Foto ${member.nickname}",
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFEEF0FF)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(18.dp))
            Column {
                Text(member.nickname, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                Text(member.nim, fontSize = 15.sp, color = TextGray)
            }
        }
        Spacer(Modifier.height(24.dp))

        // Card biodata
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                InfoRow("Nama Lengkap", member.fullName)
                HorizontalDivider(color = BorderColor)
                InfoRow("Nama Panggilan", member.nickname)
                HorizontalDivider(color = BorderColor)
                Column(modifier = Modifier.padding(vertical = 14.dp)) {
                    Text("Hobi", fontSize = 12.sp, color = TextGray)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        member.hobby.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(PurpleSoft)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PurpleDark
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(color = BorderColor)
                InfoRow("Cita-cita", member.dream)
                // Motto moved to separate card
            }
        }
        // Motto Section
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Motto", fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text(member.motto, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            }
        }
        // Video Perkenalan Section
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBgColor),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Video Perkenalan", fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                val context = LocalContext.current
                val exoPlayer = remember(context) {
                    ExoPlayer.Builder(context).build().apply {
                        // Create a mapping of member ID to video file names
                        val videoFileName = when (member.id) {
                            0 -> "intro1"
                            1 -> "intro2"
                            2 -> "intro1"
                            3 -> "intro2"
                            else -> "intro1"
                        }
                        setMediaItem(MediaItem.fromUri("android.resource://${context.packageName}/raw/$videoFileName"))
                        prepare()
                    }
                }
                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 14.dp)) {
        Text(label, fontSize = 12.sp, color = TextGray)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
    }
}