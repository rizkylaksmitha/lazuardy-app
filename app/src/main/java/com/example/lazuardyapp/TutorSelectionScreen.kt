package com.example.lazuardyapp.tutorselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.ui.screens.PrimaryColor
import com.example.lazuardyapp.ui.theme.TextColor

// --- PERBAIKAN UTAMA: Import TutorCard dari package root ---
import com.example.lazuardyapp.TutorCard
// Asumsi: data class Tutor dan val sampleTutors berada dalam package ini (com.example.lazuardyapp.tutorselection)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorSelectionScreen(
    onNavigateBack: () -> Unit,
    onTutorSelected: (Tutor) -> Unit,
    selectedSubjectName: String // Subject passed from Dashboard
) {
    var searchText by remember { mutableStateOf("") }

    // ==========================================================
    // PERBAIKAN LOGIKA FILTERING DI SINI
    // ==========================================================
    val filteredTutors = remember(searchText, selectedSubjectName) {

        // 1. FILTER BERDASARKAN SUBJEK YANG DIPILIH
        val subjectFilteredList = if (selectedSubjectName.equals("Semua Pelajaran", ignoreCase = true)) {
            // Jika nama subjek adalah placeholder default, gunakan seluruh daftar
            sampleTutors
        } else {
            // Filter daftar hanya untuk tutor yang mengajar subjek tersebut
            sampleTutors.filter { tutor ->
                tutor.subject.equals(selectedSubjectName, ignoreCase = true)
            }
        }

        // 2. Terapkan FILTER PENCARIAN pada daftar yang sudah difilter subjek
        subjectFilteredList.filter { tutor ->
            // Jika kotak pencarian kosong, lewati filter ini (kembalikan true)
            if (searchText.isEmpty()) {
                true
            } else {
                // Cari di nama tutor ATAU subjek (agar lebih fleksibel)
                tutor.name.contains(searchText, ignoreCase = true) ||
                        tutor.subject.contains(searchText, ignoreCase = true)
            }
        }
    }
    // ==========================================================

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        // Tampilkan Subjek yang sedang dilihat di TopBar
                        text = selectedSubjectName,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Pencarian") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = TextColor.copy(alpha = 0.6f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = PrimaryColor,
                    cursorColor = PrimaryColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // List of Tutors
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTutors) { tutor ->
                    TutorCard(
                        tutor = tutor,
                        onSelectTutor = onTutorSelected
                    )
                }
            }

            if (filteredTutors.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchText.isNotEmpty()) {
                            "Tidak ada tutor ditemukan untuk \"$searchText\" pada subjek $selectedSubjectName"
                        } else {
                            "Tidak ada tutor tersedia untuk subjek $selectedSubjectName saat ini."
                        },
                        fontSize = 16.sp,
                        color = TextColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}