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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.R
import com.example.lazuardyapp.SelectedPackageData
import com.example.lazuardyapp.viewmodel.PaymentViewModel
import com.example.lazuardyapp.tutorselection.sampleTutors
import com.example.lazuardyapp.tutorselection.Tutor
import java.text.NumberFormat
import java.util.Locale

data class Tutor(val id: Int, val name: String, val subject: String)
val sampleTutors = listOf(Tutor(id = 1, name = "Budi Hartono", subject = "Matematika"))

const val BASE_PRICE_PER_SESSION = 50000
val PrimaryColor = Color(0xFF3892A4)
val TextColor = Color(0xFF333333)
val DividerColor = Color(0xFFE0E0E0)
val PriceColor = Color(0xFFE53935)
val SelectedBorderColor = PrimaryColor

data class LearningPackage(
    val id: Int,
    val sessions: Int,
    val price: Int,
    val benefits: List<String>
)

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

fun formatRupiah(number: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(number).replace("Rp", "Rp ").replace(",00", "")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihPaketBelajarScreen(
    tutorId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToPayment: () -> Unit,
    paymentViewModel: PaymentViewModel = viewModel()
) {

    val selectedTutor = remember(tutorId) {
        sampleTutors.find { it.id == tutorId } ?: sampleTutors.find { it.id == 1 }
    }

    var selectedPackage by remember { mutableStateOf<LearningPackage?>(packages.firstOrNull()) }

    if (selectedTutor == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Error: Data tutor tidak ditemukan.",
                color = Color.Red,
                fontSize = 18.sp
            )
        }
        return
    }

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
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val pkg = selectedPackage
                        if (pkg != null) {

                            val selectedData = SelectedPackageData(
                                packageId = pkg.id,
                                packageName = "Paket ${pkg.sessions}x Pertemuan",
                                totalSessions = pkg.sessions,
                                tutorName = selectedTutor.name,
                                subject = selectedTutor.subject,
                                scheduleDays = listOf("Senin", "Rabu", "Jumat")
                            )

                            paymentViewModel.setPackageForPayment(selectedData)

                            onNavigateToPayment()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    enabled = selectedPackage != null
                ) {
                    Text(
                        text = "Lanjut Pembayaran (${formatRupiah(selectedPackage?.price ?: 0)})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF4F4F4)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Paket Belajar dengan ${selectedTutor.name} (${selectedTutor.subject})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(packages) { pkg ->
                PackageCard(
                    pkg = pkg,
                    isSelected = pkg.id == selectedPackage?.id,
                    onSelect = { selected ->
                        selectedPackage = selected
                    }
                )
            }
        }
    }
}

@Composable
fun PackageCard(pkg: LearningPackage, isSelected: Boolean, onSelect: (LearningPackage) -> Unit) {
    val borderColor = if (isSelected) SelectedBorderColor else DividerColor
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(pkg) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_book_light),
                    contentDescription = "Ikon Paket",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PriceColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Terpilih",
                        tint = SelectedBorderColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Kamu akan mendapatkan:",
                fontSize = 14.sp,
                color = TextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            pkg.benefits.forEach { benefit ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = PrimaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        fontSize = 14.sp,
                        color = TextColor.copy(alpha = 0.9f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}