package com.example.lazuardyapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import java.util.*

data class UserProfile(
    val namaLengkap: String = "",
    val email: String,
    val telepon: String,
    val jenisKelamin: String = "",
    val tanggalLahir: String = "",
    val agama: String = "",
    val nomorWhatsApp: String = "",
    val namaOrtuWali: String = "Kurniawati",
    val nomorWaOrtuWali: String = "(+62) 81122334455",
    val provinsi: String = "",
    val kotaKabupaten: String = "",
    val kecamatan: String = "",
    val desaKelurahan: String = "",
    val alamatLengkap: String = "",
    val asalSekolah: String = "",
    val kelas: String = ""
)

fun createInitialProfile(email: String, telepon: String): UserProfile {
    return UserProfile(namaLengkap = "Andika Putra", email = email, telepon = telepon, nomorWhatsApp = telepon)
}

val InitialEmptyProfile = createInitialProfile(email = "andikaputra@gmail.com", telepon = "(+62) 81234567890")
val PrimaryColor = Color(0xFF2C8AA4)
val TextColor = Color(0xFF5E6B7B)
val BackgroundColor = Color(0xFFF7F9FA)
val InputBorderColor = Color(0xFFC7D0D8)

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onNavigateToHome: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 2,
                onNavigateToHome = onNavigateToHome,
                onNavigateToJadwal = onNavigateToJadwal,
                onNavigateToProfile = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Profil",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))

            ProfileCard(userProfile, onNavigateToEditProfile)
            Spacer(modifier = Modifier.height(24.dp))
            DetailAlamatCard(userProfile)
            Spacer(modifier = Modifier.height(24.dp))
            DetailSekolahCard(userProfile)
            Spacer(modifier = Modifier.height(24.dp))
            ParentGuardianCard(userProfile)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileCard(userProfile: UserProfile, onEditClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .border(1.dp, TextColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.namaLengkap.ifEmpty { "Nama Belum Diisi" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = userProfile.email,
                        fontSize = 14.sp,
                        color = TextColor
                    )
                    Text(
                        text = userProfile.nomorWhatsApp.ifEmpty { userProfile.telepon },
                        fontSize = 14.sp,
                        color = TextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth(0.6f).height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profil",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profil", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun DetailAlamatCard(userProfile: UserProfile) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Detail Alamat", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Divider(color = TextColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
            ProfileDetailRow(label = "Provinsi", value = userProfile.provinsi)
            ProfileDetailRow(label = "Kota/Kabupaten", value = userProfile.kotaKabupaten)
            ProfileDetailRow(label = "Kecamatan", value = userProfile.kecamatan)
            ProfileDetailRow(label = "Desa/Kelurahan", value = userProfile.desaKelurahan)
            ProfileDetailRow(label = "Alamat Lengkap", value = userProfile.alamatLengkap)
        }
    }
}

@Composable
fun DetailSekolahCard(userProfile: UserProfile) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Detail Sekolah", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Divider(color = TextColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
            ProfileDetailRow(label = "Asal Sekolah", value = userProfile.asalSekolah)
            ProfileDetailRow(label = "Kelas", value = userProfile.kelas)
        }
    }
}

@Composable
fun ParentGuardianCard(userProfile: UserProfile) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kontak Orangtua/Wali", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Divider(color = TextColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
            ProfileDetailRow(label = "Nama Orangtua/Wali", value = userProfile.namaOrtuWali)
            ProfileDetailRow(label = "No Telepon", value = userProfile.nomorWaOrtuWali)
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp)
    ) {
        Text(text = label, fontSize = 14.sp, color = TextColor)

        Text(
            text = value.ifEmpty { "-" },
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Divider(color = TextColor.copy(alpha = 0.3f), thickness = 0.5.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentProfile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit,
    onNavigateBack: () -> Unit
) {
    var namaLengkap by remember { mutableStateOf(currentProfile.namaLengkap) }
    var jenisKelamin by remember { mutableStateOf(currentProfile.jenisKelamin) }
    var tanggalLahir by remember { mutableStateOf(currentProfile.tanggalLahir) }
    var agama by remember { mutableStateOf(currentProfile.agama) }
    var nomorWhatsApp by remember { mutableStateOf(currentProfile.nomorWhatsApp) }
    var namaOrtuWali by remember { mutableStateOf(currentProfile.namaOrtuWali) }
    var nomorWaOrtuWali by remember { mutableStateOf(currentProfile.nomorWaOrtuWali) }
    var provinsi by remember { mutableStateOf(currentProfile.provinsi) }
    var kotaKabupaten by remember { mutableStateOf(currentProfile.kotaKabupaten) }
    var kecamatan by remember { mutableStateOf(currentProfile.kecamatan) }
    var desaKelurahan by remember { mutableStateOf(currentProfile.desaKelurahan) }
    var alamatLengkap by remember { mutableStateOf(currentProfile.alamatLengkap) }
    var asalSekolah by remember { mutableStateOf(currentProfile.asalSekolah) }
    var kelas by remember { mutableStateOf(currentProfile.kelas) }

    val scrollState = rememberScrollState()
    val screenHorizontalPadding = 32.dp

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = screenHorizontalPadding)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            EditFormCard("Detail Pribadi") {
                FormTextField(label = "Nama Lengkap", value = namaLengkap, onValueChange = { namaLengkap = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Jenis Kelamin", value = jenisKelamin, onValueChange = { jenisKelamin = it }, placeholder = "Laki-laki atau Perempuan", keyboardType = KeyboardType.Text, readOnly = false, modifier = Modifier)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Tanggal Lahir", value = tanggalLahir, onValueChange = { tanggalLahir = it }, placeholder = "DD/MM/YYYY", keyboardType = KeyboardType.Number, readOnly = false, modifier = Modifier, trailingIcon = { Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Pilih Tanggal", tint = TextColor) })
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Agama", value = agama, onValueChange = { agama = it }, placeholder = "Islam, Kristen, Katolik, dst.", keyboardType = KeyboardType.Text, readOnly = false, modifier = Modifier)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Nomor WhatsApp", value = nomorWhatsApp, onValueChange = { nomorWhatsApp = it }, placeholder = "Contoh: 081234567890", keyboardType = KeyboardType.Phone)
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditFormCard("Detail Alamat") {
                FormTextField(label = "Provinsi", value = provinsi, onValueChange = { provinsi = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Kota/Kabupaten", value = kotaKabupaten, onValueChange = { kotaKabupaten = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Kecamatan", value = kecamatan, onValueChange = { kecamatan = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Desa/Kelurahan", value = desaKelurahan, onValueChange = { desaKelurahan = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Alamat Lengkap", value = alamatLengkap, onValueChange = { alamatLengkap = it }, keyboardType = KeyboardType.Text)
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditFormCard("Detail Sekolah") {
                FormTextField(label = "Asal Sekolah", value = asalSekolah, onValueChange = { asalSekolah = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Kelas", value = kelas, onValueChange = { kelas = it }, keyboardType = KeyboardType.Text)
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditFormCard("Kontak Orang Tua/Wali") {
                FormTextField(label = "Nama Orang Tua/Wali", value = namaOrtuWali, onValueChange = { namaOrtuWali = it }, keyboardType = KeyboardType.Text)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Nomor WhatsApp", value = nomorWaOrtuWali, onValueChange = { nomorWaOrtuWali = it }, placeholder = "Contoh: 081234567890", keyboardType = KeyboardType.Phone)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val updatedProfile = currentProfile.copy(
                        namaLengkap = namaLengkap, jenisKelamin = jenisKelamin, tanggalLahir = tanggalLahir, agama = agama, nomorWhatsApp = nomorWhatsApp, namaOrtuWali = namaOrtuWali, nomorWaOrtuWali = nomorWaOrtuWali,
                        provinsi = provinsi, kotaKabupaten = kotaKabupaten, kecamatan = kecamatan, desaKelurahan = desaKelurahan, alamatLengkap = alamatLengkap, asalSekolah = asalSekolah, kelas = kelas
                    )
                    onSaveProfile(updatedProfile)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Simpan", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun EditFormCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Divider(color = TextColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = TextColor, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.Gray) },
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            readOnly = readOnly,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = InputBorderColor,
                cursorColor = PrimaryColor
            )
        )
    }
}

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val primaryColor = Color(0xFF2C8AA4)
    val unselectedColor = Color(0xFF8C8C8C)
    val indicatorBg = Color(0xFFE5F1F4)
    val indicatorIconColor = Color(0xFF3892A4)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 12.dp, top = 4.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .height(76.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf("Beranda", "Jadwal", "Profil")
            val iconVectors = listOf(
                Icons.Outlined.Home to Icons.Filled.Home,
                Icons.Outlined.CalendarToday to Icons.Filled.CalendarToday,
                Icons.Outlined.Person to Icons.Filled.Person
            )

            items.forEachIndexed { index, label ->
                val isSelected = selectedItem == index

                val onClickAction = when (index) {
                    0 -> onNavigateToHome
                    1 -> onNavigateToJadwal
                    2 -> onNavigateToProfile
                    else -> { {} }
                }

                Column(
                    modifier = Modifier
                        .clickable(onClick = onClickAction)
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) indicatorBg else Color.Transparent)
                            .size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) iconVectors[index].second else iconVectors[index].first,
                            contentDescription = label,
                            modifier = Modifier.size(26.dp),
                            tint = if (isSelected) indicatorIconColor else unselectedColor
                        )
                    }

                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) primaryColor else unselectedColor
                    )
                }
            }
        }
    }
}