package com.example.lazuardyapp.pilihpaket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image // <-- Import Image dipertahankan
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
import androidx.compose.ui.res.painterResource // <-- Import painterResource dipertahankan
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.R
import java.text.NumberFormat
import java.util.Locale

// --- ASUMSI DATA & WARNA ---
data class Tutor(val id: Int, val name: String, val subject: String)
val sampleTutor = Tutor(id = 1, name = "Budi Santoso", subject = "Matematika")

// PERBAIKAN: Mengatur harga dasar per sesi agar sesuai dengan gambar (4 sesi = 200.000)
const val BASE_PRICE_PER_SESSION = 50000

val PrimaryColor = Color(0xFF3892A4) // Warna utama (Teal/Biru)
val PackageBorderColor = PrimaryColor
val TextColor = Color(0xFF333333)
val CheckColor = PrimaryColor
val DividerColor = Color(0xFFE0E0E0)
val PriceColor = Color(0xFFE53935) // Warna Merah untuk Harga (sesuai gambar)

// --- DATA MODEL PAKET BELAJAR (DITAMBAH ID) ---
data class LearningPackage(
    val id: Int,
    val sessions: Int,
    val price: Int,
    val benefits: List<String>
)

// List paket belajar (4, 8, 12, 16 pertemuan)
val packages = listOf(4, 8, 12, 16).map { sessions ->
    LearningPackage(
        id = sessions,
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
    onNavigateBack: () -> Unit,
    onNavigateToPayment: (packageId: Int) -> Unit
) {
    var selectedPackage by remember { mutableStateOf<LearningPackage?>(packages.firstOrNull()) }
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
                .background(Color(0xFFF4F4F4)), // Background abu-abu muda
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Info Tutor (Dipertahankan, meskipun tidak ada di gambar Pilih Paket)
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
                    // Di layar Pilih Paket, tidak ada indikasi "Terpilih" yang mencolok
                    isSelected = false,
                    onSelect = { selected ->
                        selectedPackage = selected
                        onNavigateToPayment(selected.id)
                    }
                )
            }

            // Spacer akhir
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// =================================================================
// KOMPONEN CARD PAKET (Diperbarui agar sama persis dengan gambar)
// =================================================================

@Composable
fun PackageCard(pkg: LearningPackage, isSelected: Boolean, onSelect: (LearningPackage) -> Unit) {

    // Border berwarna abu-abu tipis seperti di gambar
    val borderColor = Color(0xFFE0E0E0)
    val buttonContainerColor = PrimaryColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
        // Di gambar, klik tombol yang memicu aksi, bukan klik Card secara keseluruhan
        // .clickable { onSelect(pkg) },
        ,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor), // Border lebih tipis (1.dp)
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Tanpa shadow
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- HEADER PAKET (Icon + Text) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Buku (Digambar sebagai Image resource)
                Image(
                    painter = painterResource(id = R.drawable.ic_book_light),
                    contentDescription = "Ikon Paket",
                    modifier = Modifier.size(40.dp) // Ukuran ikon disesuaikan
                )
                Spacer(modifier = Modifier.width(12.dp)) // Spacer sedikit dikurangi
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
                        fontSize = 18.sp, // Ukuran harga disesuaikan
                        fontWeight = FontWeight.Bold,
                        color = PriceColor // Warna Harga Merah
                    )
                }
            }

            // Garis Pemisah (Tidak ada di gambar antara harga dan benefit)
            // Divider(modifier = Modifier.padding(vertical = 12.dp), color = DividerColor)

            Spacer(modifier = Modifier.height(16.dp))

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
                        .padding(vertical = 2.dp), // Padding vertical dikurangi
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = PrimaryColor, // Warna ceklist biru
                        modifier = Modifier.size(16.dp) // Ukuran ceklist dikurangi
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        fontSize = 14.sp,
                        color = TextColor.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TOMBOL PILIH PAKET ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // Ditarik ke kanan
            ) {
                Button(
                    onClick = { onSelect(pkg) },
                    modifier = Modifier
                        .height(40.dp)
                        .width(120.dp), // Lebar tombol disesuaikan
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonContainerColor)
                ) {
                    Text(
                        text = "Pilih Paket", // Selalu "Pilih Paket" di layar ini
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}