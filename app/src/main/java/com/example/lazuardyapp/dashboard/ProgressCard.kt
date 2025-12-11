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
fun ProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Progres Belajar",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Paket 4 Pertemuan - Matematika",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor
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
                        .border(1.dp, TextColor.copy(alpha = 0.5f), CircleShape),
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
                        text = "Budi Santoso",
                        fontSize = 14.sp,
                        color = TextColor,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Selasa, 24 September 2025 - 16:00",
                        fontSize = 12.sp,
                        color = TextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sisa Pertemuan",
                    fontSize = 14.sp,
                    color = TextColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "2/4",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, TextColor.copy(alpha = 0.5f), shape = CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ){
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(TextColor.copy(alpha = 0.8f))
                )
            }
        }
    }
}