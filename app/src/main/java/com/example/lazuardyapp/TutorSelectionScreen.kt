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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorSelectionScreen(
    onNavigateBack: () -> Unit,
    onTutorSelected: (Tutor) -> Unit,
    selectedSubjectName: String
) {
    var searchText by remember { mutableStateOf("") }

    val filteredTutors = remember(searchText, selectedSubjectName) {
        sampleTutors
            .filter { tutor ->

                val matchesSubject = if (selectedSubjectName == "Semua Pelajaran") {
                    true
                } else {
                    tutor.subject.equals(selectedSubjectName, ignoreCase = true)
                }

                val matchesSearch = tutor.name.contains(searchText, ignoreCase = true) ||
                        tutor.subject.contains(searchText, ignoreCase = true)

                matchesSubject && matchesSearch
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pilih Tutor - ${if (selectedSubjectName == "Semua Pelajaran") "Semua" else selectedSubjectName}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 18.sp
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
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Pencarian Nama atau Subjek") },
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
                        text = "Tidak ada tutor ditemukan untuk kriteria ini.",
                        fontSize = 16.sp,
                        color = TextColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}