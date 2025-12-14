package com.example.lazuardyapp.pilihpaket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.R
import java.text.NumberFormat
import java.util.Locale

// --- ASUMSI DATA & WARNA ---
data class Tutor(val id: Int, val name: String, val subject: String)
val sampleTutor = Tutor(id = 1, name = "Budi Santoso", subject = "Matematika")

const val BASE_PRICE_PER_SESSION = 75000
val PrimaryColor = Color(0xFF3892A4) // Warna utama (Teal/Biru)
val PackageBorderColor = PrimaryColor // Menggunakan PrimaryColor untuk border & harga
val TextColor = Color(0xFF333333)
val CheckColor = PrimaryColor // Menggunakan warna utama untuk checkmark (sesuai desain)
val DividerColor = Color(0xFFE0E0E0) // Garis pemisah yang tipis

// --- DATA MODEL PAKET BELAJAR ---
data class LearningPackage(
    val sessions: Int,
    val price: Int,
    val benefits: List<String>
)

// List paket belajar (4, 8, 12, 16 pertemuan)
val packages = listOf(4, 8, 12, 16).map { sessions ->
    LearningPackage(
        sessions = sessions,
        price = sessions * BASE_PRICE_PER_SESSION,
        benefits = listOf(
            "$sessions sesi privat setiap pertemuan @90 menit",
            "Modul belajar eksklusif",
            "Soal-soal dan pembahasan",
            "Konsultasi via WhatsApp"
        )
    )
}

// Fungsi format Rupiah
fun formatRupiah(number: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(number).replace("Rp", "Rp ").replace(",00", "")
}

// =================================================================
// KOMPONEN UTAMA
// =================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihPaketBelajarScreen(
    tutorId: Int,
    onNavigateBack: () -> Unit
) {
    var selectedPackage by remember { mutableStateOf<LearningPackage?>(packages.firstOrNull()) } // Default ke paket pertama
    val tutor = if (tutorId == sampleTutor.id) sampleTutor else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pilih Paket Belajar",
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
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF4F4F4)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Tampilkan info Tutor jika ada (Opsional, tergantung desain akhir)
            item {
                if (tutor != null) {
                    Text(
                        text = "Paket Belajar dengan ${tutor.name} (${tutor.subject})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // List Kartu Paket
            items(packages) { pkg ->
                PackageCard(
                    pkg = pkg,
                    isSelected = pkg == selectedPackage,
                    onSelect = { selectedPackage = it }
                )
            }

            // Tombol Konfirmasi Pembayaran
            item {
                Button(
                    onClick = {
                        // Aksi navigasi ke pembayaran
                    },
                    enabled = selectedPackage != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 16.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        disabledContainerColor = PrimaryColor.copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        text = "Lanjutkan Pembayaran ${if (selectedPackage != null) "(${formatRupiah(selectedPackage!!.price)})" else ""}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// =================================================================
// KOMPONEN CARD PAKET (Sangat mirip Desain)
// =================================================================

@Composable
fun PackageCard(pkg: LearningPackage, isSelected: Boolean, onSelect: (LearningPackage) -> Unit) {

    val borderColor = if (isSelected) PackageBorderColor else Color(0xFFE0E0E0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(pkg) },
        shape = RoundedCornerShape(16.dp), // Radius yang lebih besar
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- HEADER PAKET (Ikon Buku, Nama Paket, Harga) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Buku (Harus ada di R.drawable Anda)
                Image(
                    painter = painterResource(id = R.drawable.ic_book_light),
                    contentDescription = "Ikon Paket",
                    modifier = Modifier.size(50.dp) // Ukuran Ikon sesuai desain
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Paket ${pkg.sessions}x Pertemuan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatRupiah(pkg.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold, // Font Bold
                        color = PackageBorderColor // Menggunakan warna Primary/Teal
                    )
                }
            }

            // Garis Pemisah yang Tipis
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = DividerColor)

            Text(
                text = "Kamu akan mendapatkan:",
                fontSize = 14.sp,
                color = TextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // --- LIST BENEFIT ---
            pkg.benefits.forEach { benefit ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = CheckColor, // Warna Checkmark Biru/Teal
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        fontSize = 14.sp, // Ukuran teks benefit 14sp
                        color = TextColor.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TOMBOL PILIH PAKET ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onSelect(pkg) },
                    modifier = Modifier
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Text(
                        text = "Pilih Paket",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}