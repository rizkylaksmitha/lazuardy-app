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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.google.gson.Gson
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.viewmodel.ProfileViewModel
import java.util.*

data class UserProfile(
    val namaLengkap: String = "",
    val email: String,
    val telepon: String,
    val jenisKelamin: String = "",
    val tanggalLahir: String = "",
    val agama: String = "",
    val nomorWhatsApp: String = "",
    val namaOrtuWali: String = "",
    val nomorWaOrtuWali: String = "",
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
    viewModel: ProfileViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEditProfile: (UserProfile) -> Unit,
) {
    val userProfile = viewModel.profileData
    val isLoading = viewModel.isLoading
    val statusMessage = viewModel.statusMessage

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 2,
                onNavigateToHome = onNavigateToHome,
                onNavigateToJadwal = onNavigateToJadwal,
                onNavigateToProfile = onNavigateToProfile
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

            if (isLoading && userProfile.namaLengkap.contains("Memuat")) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                Text("Memuat data profil...", color = TextColor)
            } else {

                if (statusMessage.isNotEmpty()) {
                    Text(
                        text = statusMessage,
                        color = PrimaryColor,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                ProfileCard(
                    userProfile,
                    onEditClick = {
                        val gson = Gson()
                        val profileJson = gson.toJson(userProfile)
                        val encodedJson = URLEncoder.encode(profileJson, StandardCharsets.UTF_8.toString())

                        onNavigateToEditProfile(userProfile)},
                    onLogoutClick = { viewModel.logout() }
                )
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentProfile: UserProfile,
    onSaveProfile: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
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
                    IconButton(onClick = onNavigateBack, enabled = !viewModel.isLoading) {
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
                FormTextField(label = "Nama Lengkap", value = namaLengkap, onValueChange = { namaLengkap = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Email", value = currentProfile.email, onValueChange = { /* read only */ }, keyboardType = KeyboardType.Email, readOnly = true)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Jenis Kelamin", value = jenisKelamin, onValueChange = { jenisKelamin = it }, placeholder = "Laki-laki atau Perempuan", keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Tanggal Lahir", value = tanggalLahir, onValueChange = { tanggalLahir = it }, placeholder = "DD/MM/YYYY", keyboardType = KeyboardType.Number, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Agama", value = agama, onValueChange = { agama = it }, placeholder = "Islam, Kristen, Katolik, dst.", keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Nomor WhatsApp", value = nomorWhatsApp, onValueChange = { nomorWhatsApp = it }, placeholder = "Contoh: 081234567890", keyboardType = KeyboardType.Phone, readOnly = viewModel.isLoading)
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditFormCard("Detail Alamat") {
                FormTextField(label = "Provinsi", value = provinsi, onValueChange = { provinsi = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Kota/Kabupaten", value = kotaKabupaten, onValueChange = { kotaKabupaten = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Kecamatan", value = kecamatan, onValueChange = { kecamatan = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Desa/Kelurahan", value = desaKelurahan, onValueChange = { desaKelurahan = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Alamat Lengkap", value = alamatLengkap, onValueChange = { alamatLengkap = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditFormCard("Detail Sekolah") {
                FormTextField(label = "Asal Sekolah", value = asalSekolah, onValueChange = { asalSekolah = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Kelas", value = kelas, onValueChange = { kelas = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditFormCard("Kontak Orang Tua/Wali") {
                FormTextField(label = "Nama Orang Tua/Wali", value = namaOrtuWali, onValueChange = { namaOrtuWali = it }, keyboardType = KeyboardType.Text, readOnly = viewModel.isLoading)
                Spacer(modifier = Modifier.height(16.dp))
                FormTextField(label = "Nomor WhatsApp", value = nomorWaOrtuWali, onValueChange = { nomorWaOrtuWali = it }, placeholder = "Contoh: 081234567890", keyboardType = KeyboardType.Phone, readOnly = viewModel.isLoading)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val updatedProfile = currentProfile.copy(
                        namaLengkap = namaLengkap, jenisKelamin = jenisKelamin, tanggalLahir = tanggalLahir, agama = agama, nomorWhatsApp = nomorWhatsApp, namaOrtuWali = namaOrtuWali, nomorWaOrtuWali = nomorWaOrtuWali,
                        provinsi = provinsi, kotaKabupaten = kotaKabupaten, kecamatan = kecamatan, desaKelurahan = desaKelurahan, alamatLengkap = alamatLengkap, asalSekolah = asalSekolah, kelas = kelas,
                        telepon = nomorWhatsApp
                    )
                    viewModel.saveProfile(updatedProfile, onSuccess = onSaveProfile)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                shape = RoundedCornerShape(12.dp),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(text = "Simpan", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileCard(
    userProfile: UserProfile,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
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
                        text = userProfile.nomorWhatsApp.ifEmpty { userProfile.telepon }.ifEmpty { "Nomor WA Belum Diisi" },
                        fontSize = 14.sp,
                        color = TextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(end = 8.dp),
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

                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}
@Composable
fun DetailAlamatCard(userProfile: UserProfile) {
    EditFormCard("Detail Alamat") {
        ProfileDetailRow("Provinsi", userProfile.provinsi.ifEmpty { "-" })
        ProfileDetailRow("Kota/Kabupaten", userProfile.kotaKabupaten.ifEmpty { "-" })
        ProfileDetailRow("Kecamatan", userProfile.kecamatan.ifEmpty { "-" })
        ProfileDetailRow("Desa/Kelurahan", userProfile.desaKelurahan.ifEmpty { "-" })
        ProfileDetailRow("Alamat Lengkap", userProfile.alamatLengkap.ifEmpty { "-" })
    }
}

@Composable
fun DetailSekolahCard(userProfile: UserProfile) {
    EditFormCard("Detail Sekolah") {
        ProfileDetailRow("Asal Sekolah", userProfile.asalSekolah.ifEmpty { "-" })
        ProfileDetailRow("Kelas", userProfile.kelas.ifEmpty { "-" })
    }
}

@Composable
fun ParentGuardianCard(userProfile: UserProfile) {
    EditFormCard("Kontak Orang Tua/Wali") {
        ProfileDetailRow("Nama Ortu/Wali", userProfile.namaOrtuWali.ifEmpty { "-" })
        ProfileDetailRow("Nomor WA Ortu", userProfile.nomorWaOrtuWali.ifEmpty { "-" })
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, color = TextColor, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = InputBorderColor.copy(alpha = 0.5f),
            thickness = 0.5.dp
        )
    }
}

@Composable
fun EditFormCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
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
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextColor)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.Gray) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = InputBorderColor
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(50.dp),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavBarItem(
                    icon = if (selectedItem == 0) Icons.Filled.Home else Icons.Outlined.Home,
                    label = "Beranda",
                    isSelected = selectedItem == 0,
                    onClick = onNavigateToHome
                )
                NavBarItem(
                    icon = if (selectedItem == 1) Icons.Filled.DateRange else Icons.Outlined.DateRange,
                    label = "Jadwal",
                    isSelected = selectedItem == 1,
                    onClick = onNavigateToJadwal
                )
                NavBarItem(
                    icon = if (selectedItem == 2) Icons.Filled.Person else Icons.Outlined.Person,
                    label = "Profil",
                    isSelected = selectedItem == 2,
                    onClick = onNavigateToProfile
                )
            }
        }
    }
}

@Composable
fun NavBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 48.dp, height = 28.dp)
                .background(
                    color = if (isSelected) Color(0xFFE0F2F1) else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) PrimaryColor else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) PrimaryColor else Color.Gray
        )
    }
}