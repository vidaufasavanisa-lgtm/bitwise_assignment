package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFDDD6FE), Color(0xFFEEF0FF)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    member.name.first().uppercase(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleDark
                )
            }
            Spacer(Modifier.width(18.dp))
            Column {
                Text(member.name, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                Text(member.role, fontSize = 15.sp, color = TextGray)
            }
        }
        Spacer(Modifier.height(24.dp))

        // Card biodata
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                InfoRow("Nama", member.name)
                HorizontalDivider(color = BorderColor)
                InfoRow("Hobi", member.hobby)
                HorizontalDivider(color = BorderColor)
                InfoRow("Cita-cita", member.dream)
                HorizontalDivider(color = BorderColor)
                InfoRow("Tentang saya", member.about)
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