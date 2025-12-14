package com.example.lazuardyapp.paymentscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.R
import com.example.lazuardyapp.pilihpaket.packages
import com.example.lazuardyapp.pilihpaket.formatRupiah
import com.example.lazuardyapp.pilihpaket.LearningPackage // Import model paket

// --- KONSTANTA & WARNA ---
val PrimaryColor = Color(0xFF3892A4) // Warna utama (Teal/Biru)
val TextColor = Color(0xFF333333)
val CheckColor = Color(0xFF1CB455) // Hijau untuk tanda cek (sesuai gambar)
val PackageCardColor = Color(0xFFE1F5FE) // Lighter blue/cyan background (sesuai gambar)
val PriceColor = Color(0xFFE53935) // Merah untuk Harga (sesuai gambar)
val BankBorderColor = Color(0xFFE0E0E0)
val PackageBorderColor = Color(0xFF90CAF9) // Border card paket

// --- MODEL DATA UNTUK BANK ---
data class Bank(
    val id: Int,
    val name: String,
    val account: String,
    val logoResId: Int
)

// Data Dummy Bank (sesuai urutan dan logo di gambar)
// CATATAN: Ganti R.drawable.xxx dengan resource ID logo Anda yang sebenarnya
val dummyBanks = listOf(
    Bank(1, "BCA", "XXXXXXXXX", R.drawable.ic_bca),
    Bank(2, "BRI", "XXXXXXXXX", R.drawable.ic_bri),
    Bank(3, "BNI", "XXXXXXXXX", R.drawable.ic_bni),
    Bank(4, "BSI", "XXXXXXXXX", R.drawable.ic_bsi),
    Bank(5, "Mandiri", "XXXXXXXXX", R.drawable.ic_mandiri)
)

// --- MEMBUAT DATA PAKET LENGKAP UNTUK LAYAR PEMBAYARAN ---
// Mengambil data dasar dari 'packages' dan menambahkan benefit lengkap
fun getFullPackageDetails(basePackage: LearningPackage): LearningPackage {
    // Benefit tambahan sesuai gambar
    val fullBenefits = basePackage.benefits + listOf(
        "Kuis interaktif untuk melatih dan mengukur pemahaman materi",
        "Pendampingan tugas dan pr sekolah",
        "Laporan perkembangan belajar rutin"
    )
    return basePackage.copy(benefits = fullBenefits)
}

// =================================================================
// KOMPONEN UTAMA (PaymentScreen)
// =================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    packageId: Int, // Menerima ID paket yang dipilih
    onNavigateBack: () -> Unit,
    onPaymentConfirmed: () -> Unit = {} // Aksi dummy setelah konfirmasi transfer
) {
    // Mencari detail paket
    val basePackage = packages.find { it.id == packageId }
    val selectedPackage = basePackage?.let { getFullPackageDetails(it) }

    var selectedBank by remember { mutableStateOf<Bank?>(dummyBanks.firstOrNull()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pembayaran", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            // Tombol "Saya Sudah Transfer"
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Button(
                    onClick = onPaymentConfirmed,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Text("Saya Sudah Transfer", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. CARD INFORMASI PAKET
            item {
                if (selectedPackage != null) {
                    PackageInfoCard(selectedPackage)
                } else {
                    Text("Error: Detail paket tidak ditemukan.")
                }
            }

            // 2. HEADER PILIH BANK
            item {
                Text(
                    text = "Pilih Bank untuk Pembayaran",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor
                )
            }

            // 3. LIST BANK (Bank Selection Card)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)) // Corner radius sesuai gambar
                        .background(Color.White)
                        .border(1.dp, BankBorderColor, RoundedCornerShape(12.dp))
                ) {
                    dummyBanks.forEachIndexed { index, bank ->
                        BankSelectionItem(
                            bank = bank,
                            isSelected = bank == selectedBank,
                            onSelect = { selectedBank = it }
                        )
                        // Garis pemisah, kecuali item terakhir
                        if (index < dummyBanks.size - 1) {
                            Divider(color = BankBorderColor, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

// =================================================================
// KOMPONEN CARD INFORMASI PAKET
// =================================================================

@Composable
fun PackageInfoCard(pkg: LearningPackage) {
    val formattedPrice = formatRupiah(pkg.price)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Radius lebih besar di card utama
        colors = CardDefaults.cardColors(containerColor = PackageCardColor),
        border = BorderStroke(1.dp, PackageBorderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Icon + Nama Paket + Harga
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Buku
                Image(
                    painter = painterResource(id = R.drawable.ic_book_light), // Ganti dengan resource buku Anda
                    contentDescription = "Ikon Paket",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Paket ${pkg.sessions}x Pertemuan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = formattedPrice,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PriceColor // Warna merah
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = PackageBorderColor)

            Text(
                text = "Kamu akan mendapatkan:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // List Benefit
            pkg.benefits.forEach { benefit ->
                BenefitRow(benefit = benefit)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = PackageBorderColor)

            // Row Harga Final (Bottom Row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Harga",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )
                Text(
                    text = formattedPrice,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PriceColor
                )
            }
        }
    }
}

@Composable
fun BenefitRow(benefit: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Check",
            tint = CheckColor,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = benefit,
            fontSize = 14.sp,
            color = TextColor.copy(alpha = 0.9f),
            modifier = Modifier.weight(1f)
        )
    }
}


// =================================================================
// KOMPONEN ITEM PILIH BANK
// =================================================================

@Composable
fun BankSelectionItem(bank: Bank, isSelected: Boolean, onSelect: (Bank) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(bank) }
            .background(if (isSelected) Color(0xFFF0F8FF) else Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Bank
        Image(
            painter = painterResource(id = bank.logoResId),
            contentDescription = bank.name,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Bank ${bank.name}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColor
            )
            // Nomor VA
            Text(
                text = "XXXXXXXXX",
                fontSize = 14.sp,
                color = TextColor.copy(alpha = 0.7f)
            )
        }

        // Ikon Copy
        Icon(
            painter = painterResource(id = R.drawable.ic_copy), // Ganti dengan resource ikon copy Anda
            contentDescription = "Salin Nomor VA",
            tint = Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .clickable { /* Logika Salin */ }
        )
    }
}