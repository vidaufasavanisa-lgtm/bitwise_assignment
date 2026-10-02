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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---- Warna yang dipakai di seluruh aplikasi ----
val BgColor = Color(0xFFF5F6FA)
val Purple = Color(0xFF6C5CE7)
val PurpleDark = Color(0xFF4B3FB5)
val PurpleSoft = Color(0xFFEDE9FE)
val TextDark = Color(0xFF111827)
val TextGray = Color(0xFF6B7280)
val BorderColor = Color(0xFFE5E7EB)

// ---- Logo "Bitwise" (dipakai di Home & Profile) ----
@Composable
fun AppHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = TextDark)) { append("Bit") }
                withStyle(SpanStyle(color = Purple)) { append("wise") }
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(TextDark),
            contentAlignment = Alignment.Center
        ) {
            Text("BW", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun HomeScreen(onMemberClick: (Int) -> Unit) {
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
                AppHeader()
                Spacer(Modifier.height(24.dp))

                Text(
                    "STUDENT PROFILE",
                    color = Purple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Kenalan dengan\ntim Bitwise.",
                    color = TextDark,
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tempat sederhana untuk mengenal anggota tim, mulai dari biodata sampai hal-hal menarik tentang mereka.",
                    color = TextGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cari nama anggota...", color = TextGray) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextGray)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Purple,
                        unfocusedBorderColor = BorderColor
                    )
                )
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Our People", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Text("${filtered.size} members", fontSize = 13.sp, color = TextGray)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Kotak avatar dengan huruf depan nama
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFDDD6FE), Color(0xFFEEF0FF)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    member.nickname.first().uppercase(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleDark
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(member.nickname, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Text(member.nim, fontSize = 12.sp, color = TextGray)
            Spacer(Modifier.height(10.dp))

            // Pill "View Profile →"
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PurpleSoft)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "View Profile →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleDark
                )
            }
        }
    }
}