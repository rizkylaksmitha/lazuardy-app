package com.example.lazuardyapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.viewmodel.ScheduleViewModel
import com.example.lazuardyapp.viewmodel.ScheduleItem
import com.example.lazuardyapp.viewmodel.ScheduleStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: ScheduleViewModel = viewModel()
) {

    val scheduleList = viewModel.scheduleList
    val isLoading = viewModel.isLoading
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))
    }

    val groupedAndSortedSchedules = remember(scheduleList) {
        scheduleList
            .groupBy { it.date }
            .toSortedMap(compareBy { dateString ->
                try {
                    LocalDate.parse(dateString, dateFormatter)
                } catch (e: Exception) {
                    LocalDate.MAX
                }
            })
            .entries.toList()
    }

    val primaryColor = PrimaryColor
    val textColor = TextColor
    val backgroundColor = BackgroundColor
    val screenHorizontalPadding = 32.dp

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 1,
                onNavigateToHome = onNavigateToHome,
                onNavigateToJadwal = onNavigateToJadwal,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = screenHorizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Jadwal Anda",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                Text("Memuat jadwal...", color = textColor)
            } else if (scheduleList.isEmpty()) { // Cek List Jadwal
                JadwalEmptyPlaceholder(primaryColor, textColor)
            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxHeight().fillMaxWidth()
                ) {
                    items(groupedAndSortedSchedules) { (date, schedules) ->

                        JadwalHeader(date = date, textColor = textColor)

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            schedules.forEach { item ->
                                JadwalCard(
                                    item = item,
                                    primaryColor = primaryColor,
                                    textColor = textColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun JadwalHeader(date: String, textColor: Color) {
    val parts = date.split(", ", limit = 2)
    val dayName = parts.getOrNull(0) ?: ""
    val datePart = parts.getOrNull(1) ?: date

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = dayName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = datePart,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun JadwalCard(
    item: ScheduleItem,
    primaryColor: Color,
    textColor: Color
) {
    val (statusText, statusColor, _) = when (item.status) {
        ScheduleStatus.SCHEDULED -> Triple("Terjadwal", Color(0xFF1976D2), true)
        ScheduleStatus.COMPLETED -> Triple("Selesai", Color(0xFF1CB455), false)
        ScheduleStatus.CANCELED -> Triple("Batal", Color(0xFFD32F2F), false)
    }

    val isOnlineSession = item.status == ScheduleStatus.SCHEDULED
    val mockMotivasi = "Belajar itu investasi, semangat terus!"

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, primaryColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Badge Online/Offline
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isOnlineSession) primaryColor.copy(alpha = 0.1f) else Color(0xFF8B0000).copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (isOnlineSession) primaryColor else Color(0xFF8B0000),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Text(
                            text = if (isOnlineSession) "Online" else "Offline",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isOnlineSession) primaryColor else Color(0xFF8B0000)
                        )
                    }
                }

                Text(
                    text = statusText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.subject,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.tutor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = textColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Tanggal",
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.date,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Waktu",
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.time,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "\"$mockMotivasi\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = textColor,
                lineHeight = 18.sp,
                style = androidx.compose.ui.text.TextStyle(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
        }
    }
}

@Composable
fun JadwalEmptyPlaceholder(primaryColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Belum Ada Jadwal Belajar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Silakan beli paket belajar terlebih dahulu",
                fontSize = 14.sp,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}