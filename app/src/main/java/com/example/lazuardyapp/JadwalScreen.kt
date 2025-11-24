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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

data class Jadwal(
    val id: Int,
    val mataPelajaran: String,
    val pengajar: String,
    val waktu: String,
    val motivasi: String,
    val isOnline: Boolean,
    val statusAktif: Boolean
)
@Composable
fun JadwalScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {

    val primaryColor = Color(0xFF2C8AA4)
    val textColor = Color(0xFF5E6B7B)
    val backgroundColor = Color(0xFFF7F9FA)

    val jadwalList = remember {
        listOf(
            Jadwal(
                id = 1,
                mataPelajaran = "Matematika",
                pengajar = "Budi Santoso",
                waktu = "16:00 - 17:30",
                motivasi = "Belajar itu investasi semangat terus!",
                isOnline = true,
                statusAktif = true
            ),
            Jadwal(
                id = 2,
                mataPelajaran = "Fisika Dasar",
                pengajar = "Siti Aisyah",
                waktu = "14:00 - 15:30",
                motivasi = "Setiap masalah punya solusi!",
                isOnline = false,
                statusAktif = true
            )
        )
    }

    val currentDate = "29 September 2025"
    val screenHorizontalPadding = 32.dp

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 1,
                onNavigateToHome = onNavigateToHome,
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

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentDate,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = textColor
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (jadwalList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada jadwal",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(jadwalList) { jadwal ->
                        JadwalCard(
                            jadwal = jadwal,
                            primaryColor = primaryColor,
                            textColor = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JadwalCard(
    jadwal: Jadwal,
    primaryColor: Color,
    textColor: Color
) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFF2C8AA4))
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
                // Chip Online/Offline
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (jadwal.isOnline) primaryColor.copy(alpha = 0.1f) else Color(0xFF8B0000).copy(alpha = 0.1f)
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
                                    color = if (jadwal.isOnline) primaryColor else Color(0xFF8B0000),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Text(
                            text = if (jadwal.isOnline) "Online" else "Offline",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (jadwal.isOnline) primaryColor else Color(0xFF8B0000)
                        )
                    }
                }

                // Teks Status
                Text(
                    text = if (jadwal.statusAktif) "Aktif" else "Tidak Aktif",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = jadwal.mataPelajaran,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = jadwal.pengajar,
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
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Waktu",
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = jadwal.waktu,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "\"${jadwal.motivasi}\"",
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
fun BottomNavigationBar(
    selectedItem: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val primaryColor = Color(0xFF2C8AA4)
    val unselectedColor = Color.Gray

    Surface(
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.height(70.dp)
        ) {
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = onNavigateToHome,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Beranda",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Beranda",
                        fontSize = 12.sp,
                        fontWeight = if (selectedItem == 0) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                selected = selectedItem == 1,
                onClick = {  },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Jadwal",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Jadwal",
                        fontSize = 12.sp,
                        fontWeight = if (selectedItem == 1) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                selected = selectedItem == 2,
                onClick = onNavigateToProfile,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Profil",
                        fontSize = 12.sp,
                        fontWeight = if (selectedItem == 2) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}