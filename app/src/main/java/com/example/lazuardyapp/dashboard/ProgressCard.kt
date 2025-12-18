package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.ui.screens.PrimaryColor
import com.example.lazuardyapp.ui.theme.TextColor

@Composable
fun ProgressCardContent(
    packageName: String,
    subject: String,
    tutor: String,
    progress: Int,
    totalSessions: Int,
    scheduleDays: List<String> = emptyList(),
    hideHeader: Boolean = false
) {
    val progressFraction = if (totalSessions > 0) progress.toFloat() / totalSessions.toFloat() else 0f
    val scheduleSummary = if (scheduleDays.isNotEmpty()) { scheduleDays.joinToString(separator = ", ") } else { "Jadwal belum tersedia" }
    val textColor = TextColor

    Column(modifier = Modifier.fillMaxWidth()) {

        if (!hideHeader) {
            Text(
                text = "Progres Belajar",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            if (packageName.isNotEmpty()) Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = "$packageName - $subject",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD9D9D9))
                    .border(1.dp, textColor.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Pengajar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = tutor,
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = scheduleSummary,
                    fontSize = 12.sp,
                    color = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Jumlah Pertemuan",
                fontSize = 14.sp,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "$progress/$totalSessions",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, textColor.copy(alpha = 0.5f), shape = CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
        ){
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progressFraction)
                    .clip(RoundedCornerShape(6.dp))
                    .background(TextColor.copy(alpha = 0.8f))
            )
        }
    }
}

@Composable
fun ProgressCard(
    packageName: String,
    subject: String,
    tutor: String,
    progress: Int,
    totalSessions: Int,
    scheduleDays: List<String> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProgressCardContent(
                packageName = packageName,
                subject = subject,
                tutor = tutor,
                progress = progress,
                totalSessions = totalSessions,
                scheduleDays = scheduleDays,
                hideHeader = false
            )
        }
    }
}
@Composable
fun ProgressPlaceholder(onNavigateToSelection: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
        ) {
            Text(
                text = "Progres Belajar",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Anda belum memiliki paket belajar aktif. Silakan beli paket belajar terlebih dahulu.",
                fontSize = 14.sp,
                color = TextColor
            )
        }
    }
}