package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.ui.screens.PrimaryColor
import kotlin.math.ceil

// --- DEFINISI DATA (DIPINDAHKAN KEMBALI UNTUK MENGATASI UNRESOLVED REFERENCE) ---

data class SubjectItem(
    val id: Int,
    val iconVector: ImageVector,
    val title: String,
    val iconColor: Color
)

val defaultSubjects = listOf(
    SubjectItem(1, Icons.Default.Calculate, "Matematika", Color(0xFFC62828)),
    SubjectItem(2, Icons.Default.FlashOn, "Fisika", Color(0xFF03A9F4)),
    SubjectItem(3, Icons.Default.Science, "Kimia", Color(0xFFFFC107)),
    SubjectItem(4, Icons.Default.LocalHospital, "Biologi", Color(0xFF4CAF50)),
    SubjectItem(5, Icons.Default.Book, "Bahasa Indonesia", Color(0xFF795548)),
    SubjectItem(6, Icons.Default.Language, "Bahasa Inggris", Color(0xFF00BCD4)),
    SubjectItem(7, Icons.Default.HistoryEdu, "Sejarah Indonesia", Color(0xFF9C27B0)),
    SubjectItem(8, Icons.Default.Apps, "Semua Pelajaran", Color(0xFF607D8B)),
)

val allSubjects = listOf(
    SubjectItem(1, Icons.Default.Calculate, "Matematika", Color(0xFFC62828)),
    SubjectItem(2, Icons.Default.FlashOn, "Fisika", Color(0xFF03A9F4)),
    SubjectItem(3, Icons.Default.Science, "Kimia", Color(0xFFFFC107)),
    SubjectItem(4, Icons.Default.LocalHospital, "Biologi", Color(0xFF4CAF50)),
    SubjectItem(5, Icons.Default.Book, "Bahasa Indonesia", Color(0xFF795548)),
    SubjectItem(6, Icons.Default.Language, "Bahasa Inggris", Color(0xFF00BCD4)),
    SubjectItem(7, Icons.Default.HistoryEdu, "Sejarah Indonesia", Color(0xFF9C27B0)),
    SubjectItem(9, Icons.Default.Policy, "PKN", Color(0xFFFFA000)),
    SubjectItem(10, Icons.Default.MonetizationOn, "Ekonomi", Color(0xFF455A64)),
    SubjectItem(11, Icons.Default.People, "Sosiologi", Color(0xFF673AB7)),
    SubjectItem(12, Icons.Default.Public, "Geografi", Color(0xFF009688)),
    SubjectItem(13, Icons.Default.Computer, "Informatika", Color(0xFF3F51B5)),
)
// --- AKHIR DEFINISI DATA ---


@Composable
fun SearchPackageCard(
    isExpanded: Boolean,
    onSubjectClick: (SubjectItem) -> Unit,
    onExpandClick: () -> Unit
){

    val subjectsToDisplay = if (isExpanded) allSubjects else defaultSubjects

    val numColumns = 4
    // Baris ini sudah benar, tetapi jika ada error kompilasi di sini,
    // pastikan Anda tidak menggunakan properti yang dihapus pada List/Array.
    // .size.toDouble() adalah cara yang benar.
    val rowCount = ceil(subjectsToDisplay.size.toDouble() / numColumns).toInt()

    val totalHeight = (rowCount * 90) + 16

    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Cari Paket Belajar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(numColumns),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(totalHeight.dp)
            ) {
                items(subjectsToDisplay) { subject ->

                    val clickAction: () -> Unit = {
                        if (subject.title == "Semua Pelajaran" && !isExpanded) {
                            onExpandClick()
                        } else {
                            // Panggil onSubjectClick dengan objek SubjectItem
                            onSubjectClick(subject)
                        }
                    }

                    SubjectGridItem(
                        subject = subject,
                        onClick = clickAction,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Perkecil Daftar",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(onClick = onExpandClick)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}