package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Surface
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

// ---- Tema ----
val LocalDarkTheme = compositionLocalOf { false }

// ---- Warna yang dipakai di seluruh aplikasi ----
val BgColor: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFF160D20) else Color.White
val Purple: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFF8B7BC4) else Color(0xFF6667AB)
val PurpleDark: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFFFFF9FF) else Color(0xFF4B3FB5)
val PurpleSoft: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFF8B7BC4) else Purple
val TextDark: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFFF3E7F0) else Color(0xFF111827)
val TextGray: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFFD8CBD8) else Color(0xFF6B7280)
val MutedText: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFF9A8BB5) else Color(0xFF6B7280)
val BorderColor: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFF5B3A68) else Color(0xFFE5E7EB)
val CardBgColor: Color @Composable get() = if (LocalDarkTheme.current) Color(0xFF21132C) else Color.White

// ---- Logo "Bitwise" (dipakai di Home & Profile) ----
@Composable
fun AppHeader(onOpenDrawer: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onOpenDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextDark)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextDark)) { append("Bit") }
                    withStyle(SpanStyle(color = Purple)) { append("wise") }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Aplikasi",
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun HomeScreen(onMemberClick: (Int) -> Unit, onOpenDrawer: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = MemberData.members.filter {
        it.nickname.contains(query, ignoreCase = true)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bagian atas (header, judul, search) memenuhi lebar penuh
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                AppHeader(onOpenDrawer = onOpenDrawer)
                Spacer(Modifier.height(24.dp))

                Text(
                    "STUDENT PROFILE",
                    color = Purple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                // Video Intro Section
                val context = LocalContext.current
                val exoPlayer = remember(context) {
                    ExoPlayer.Builder(context).build().apply {
                        val videoUri = "android.resource://${context.packageName}/${R.raw.intro}"
                        setMediaItem(MediaItem.fromUri(videoUri))
                        prepare()
                    }
                }
                // Video Player
                var showSettings by remember { mutableStateOf(false) }
                var isFullscreen by remember { mutableStateOf(false) }
                var volume by remember { mutableFloatStateOf(1f) }

                DisposableEffect(exoPlayer) {
                    onDispose { exoPlayer.release() }
                }

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBgColor),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
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
                            update = { view -> view.player = exoPlayer },
                            modifier = Modifier.fillMaxSize()
                        )

                    }
                }

                if (showSettings) {
                    AlertDialog(
                        onDismissRequest = { showSettings = false },
                        title = { Text("Pengaturan Video") },
                        text = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Volume: ${(volume * 100).toInt()}%")
                                Slider(
                                    value = volume,
                                    onValueChange = {
                                        volume = it
                                        exoPlayer.volume = it
                                    },
                                    valueRange = 0f..1f
                                )

                                Text("Playback Speed")
                                listOf(0.5f, 1f, 1.5f, 2f).forEach { speed ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                exoPlayer.setPlaybackSpeed(speed)
                                                showSettings = false
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = exoPlayer.playbackParameters.speed == speed,
                                            onClick = {
                                                exoPlayer.setPlaybackSpeed(speed)
                                                showSettings = false
                                            }
                                        )
                                        Text(
                                            "${speed}x",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showSettings = false }) {
                                Text("Tutup")
                            }
                        }
                    )
                }

                if (isFullscreen) {
                    Dialog(
                        onDismissRequest = { isFullscreen = false }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
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
                                modifier = Modifier.fillMaxSize()
                            )

                            IconButton(
                                onClick = { isFullscreen = false },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FullscreenExit,
                                    contentDescription = "Keluar Fullscreen",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    "Halo!",
                    color = TextDark,
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Kenali lebih dekat siapa saja yang ada di balik kelompok Bitwise.",
                    color = TextDark,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cari nama anggota...", color = TextDark) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextDark)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBgColor,
                        unfocusedContainerColor = CardBgColor,
                        focusedBorderColor = Purple,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    )
                )
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Our People", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Text("${filtered.size} members", fontSize = 13.sp, color = MutedText)
                }
            }
        }

        // Daftar card anggota (2 kolom)
        items(filtered, key = { it.id }) { member ->
            MemberCard(member = member, onClick = { onMemberClick(member.id) })
        }
    }
}

@Composable
fun MemberCard(member: Member, onClick: () -> Unit) {
    Card(
        onClick = onClick, // <-- ini yang bikin card bisa dipencet
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Kotak avatar dengan huruf depan nama
            Image(
                painter = painterResource(id = member.photoResId),
                contentDescription = "Foto ${member.nickname}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF0FF)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(12.dp))
            Text(member.nickname, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Text(member.nim, fontSize = 12.sp, color = MutedText)
            Spacer(Modifier.height(10.dp))

            // Pill "View Profile →"
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Purple)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "View Profile →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}