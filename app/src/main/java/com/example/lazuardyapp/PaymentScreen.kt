package com.example.lazuardyapp

import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.viewmodel.PaymentViewModel
import com.example.lazuardyapp.SelectedPackageData
import com.example.lazuardyapp.pilihpaket.packages
import com.example.lazuardyapp.pilihpaket.formatRupiah
import com.example.lazuardyapp.pilihpaket.LearningPackage
import com.example.lazuardyapp.pilihpaket.PrimaryColor
import java.text.NumberFormat
import java.util.Locale


val CheckColor = Color(0xFF1CB455)
val PackageCardColor = Color(0xFFE1F5FE)
val PriceColor = Color(0xFFE53935)
val BankBorderColor = Color(0xFFE0E0E0)
val PackageBorderColor = Color(0xFF90CAF9)

data class Bank(
    val id: Int,
    val name: String,
    val account: String,
    val logoResId: Int
)

val dummyBanks = listOf(
    Bank(1, "BCA", "XXXXXXXXX", R.drawable.ic_bca),
    Bank(2, "BRI", "XXXXXXXXX", R.drawable.ic_bri),
    Bank(3, "BNI", "XXXXXXXXX", R.drawable.ic_bni),
    Bank(4, "BSI", "XXXXXXXXX", R.drawable.ic_bsi),
    Bank(5, "Mandiri", "XXXXXXXXX", R.drawable.ic_mandiri)
)
fun getFullPackageBenefits(data: SelectedPackageData): List<String> {
    return listOf(
        "Sesi privat sebanyak ${data.totalSessions}x @90 menit",
        "Modul belajar eksklusif",
        "Soal-soal dan pembahasan",
        "Konsultasi via WhatsApp",
        "Kuis interaktif untuk melatih dan mengukur pemahaman materi",
        "Pendampingan tugas dan pr sekolah",
        "Laporan perkembangan belajar rutin"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onNavigateToDashboard: () -> Unit,
    paymentViewModel: PaymentViewModel = viewModel()
) {
    val isProcessing = paymentViewModel.isProcessing
    val selectedPackageData = paymentViewModel.selectedPackageData
    val packagePrice = packages.find { it.id == selectedPackageData?.packageId }?.price ?: 0

    var selectedBank by remember { mutableStateOf<Bank?>(dummyBanks.firstOrNull()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pembayaran", fontWeight = FontWeight.SemiBold, color = TextColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToDashboard) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Button(
                    onClick = {
                        if (selectedPackageData != null && !isProcessing) {
                            paymentViewModel.confirmTransfer(
                                packageId = selectedPackageData.packageId,
                                packageName = selectedPackageData.packageName,
                                totalSessions = selectedPackageData.totalSessions,
                                tutorName = selectedPackageData.tutorName,
                                subject = selectedPackageData.subject,
                                scheduleDays = selectedPackageData.scheduleDays,
                                onSuccess = onNavigateToDashboard
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    enabled = selectedPackageData != null && !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Saya Sudah Transfer", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { paddingValues ->

        if (selectedPackageData == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Error: Detail paket tidak ditemukan. Mohon pilih paket dari awal.", color = Color.Red, fontSize = 18.sp)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                PackageInfoCard(
                    data = selectedPackageData,
                    totalPrice = packagePrice
                )
            }

            item {
                Text(
                    text = "Pilih Bank untuk Pembayaran",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, BankBorderColor, RoundedCornerShape(12.dp))
                ) {
                    dummyBanks.forEachIndexed { index, bank ->
                        BankSelectionItem(
                            bank = bank,
                            isSelected = bank == selectedBank,
                            onSelect = { selectedBank = it }
                        )
                        if (index < dummyBanks.size - 1) {
                            Divider(color = BankBorderColor, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PackageInfoCard(data: SelectedPackageData, totalPrice: Int) {
    val formattedPrice = formatRupiah(totalPrice)
    val benefitsList = getFullPackageBenefits(data)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PackageCardColor),
        border = BorderStroke(1.dp, PackageBorderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_book_light),
                    contentDescription = "Ikon Paket",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = data.packageName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = formattedPrice,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PriceColor
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

            benefitsList.forEach { benefit ->
                BenefitRow(benefit = benefit)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = PackageBorderColor)

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
            Text(
                text = bank.account,
                fontSize = 14.sp,
                color = TextColor.copy(alpha = 0.7f)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_copy),
            contentDescription = "Salin Nomor VA",
            tint = Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .clickable {  }
        )
    }
}