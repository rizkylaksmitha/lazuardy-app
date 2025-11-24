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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CalendarToday
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
import java.util.*

// --- Data Model dan Warna ---
data class UserProfile(
    val namaLengkap: String = "",
    val email: String,
    val telepon: String,
    val jenisKelamin: String = "",
    val tanggalLahir: String = "",
    val agama: String = "",
    val nomorWhatsApp: String = "",
    val namaOrtuWali: String = "",
    val nomorWaOrtuWali: String = ""
)

fun createInitialProfile(email: String, telepon: String): UserProfile {
    return UserProfile(email = email, telepon = telepon, nomorWhatsApp = telepon)
}

val InitialEmptyProfile = createInitialProfile(email = "email_pengguna@example.com", telepon = "(+62) 8xxxxxxxxxx")
val PrimaryColor = Color(0xFF2C8AA4)
val TextColor = Color(0xFF5E6B7B)
val BackgroundColor = Color(0xFFF7F9FA)
val InputBorderColor = Color(0xFFC7D0D8)


// --- PROFILE SCREEN ---
@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onNavigateToHome: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            // Asumsi: onNavigateToJadwal sama dengan onNavigateToHome, Anda bisa menggantinya jika ada navigasi spesifik ke Jadwal.
            BottomNavigationBar(
                selectedItem = 2,
                onNavigateToHome = onNavigateToHome,
                onNavigateToJadwal = onNavigateToHome,
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
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .border(1.dp, Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.namaLengkap.ifEmpty { "Nama Belum Diisi" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (userProfile.namaLengkap.isEmpty()) TextColor else Color.Black
                    )
                    Text(
                        // Email yang terisi otomatis dari login
                        text = userProfile.email,
                        fontSize = 14.sp,
                        color = TextColor
                    )
                    Text(
                        // Nomor WhatsApp (diutamakan) atau Nomor Telepon utama
                        text = userProfile.nomorWhatsApp.ifEmpty { userProfile.telepon },
                        fontSize = 14.sp,
                        color = TextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profil",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profil", fontWeight = FontWeight.SemiBold)
            }
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
            Text(
                text = "Kontak Orangtua/Wali",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Divider(color = TextColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
            ProfileDetailRow(label = "Nama Orangtua/Wali", value = userProfile.namaOrtuWali)
            Spacer(modifier = Modifier.height(4.dp))
            ProfileDetailRow(label = "No Telepon", value = userProfile.nomorWaOrtuWali)
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = TextColor)
        Text(text = value.ifEmpty { "-" }, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}

// --- EDIT PROFILE SCREEN (Input Manual) ---
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

    val scrollState = rememberScrollState()

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
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                EditFormCard("Detail Pribadi") {
                    FormTextField(
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        onValueChange = { namaLengkap = it },
                        keyboardType = KeyboardType.Text
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FormTextField(
                        label = "Jenis Kelamin",
                        value = jenisKelamin,
                        onValueChange = { jenisKelamin = it },
                        placeholder = "Laki-laki atau Perempuan",
                        keyboardType = KeyboardType.Text,
                        readOnly = false,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FormTextField(
                        label = "Tanggal Lahir",
                        value = tanggalLahir,
                        onValueChange = { tanggalLahir = it },
                        placeholder = "DD/MM/YYYY",
                        keyboardType = KeyboardType.Number,
                        readOnly = false,
                        modifier = Modifier,
                        // Tambahkan ikon kalender di sini
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Pilih Tanggal",
                                tint = TextColor
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FormTextField(
                        label = "Agama",
                        value = agama,
                        onValueChange = { agama = it },
                        placeholder = "Islam, Kristen, Katolik, dst.",
                        keyboardType = KeyboardType.Text,
                        readOnly = false,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FormTextField(label = "Nomor WhatsApp", value = nomorWhatsApp, onValueChange = { nomorWhatsApp = it }, placeholder = "Contoh: 081234567890", keyboardType = KeyboardType.Phone)
                }

                Spacer(modifier = Modifier.height(24.dp))

                EditFormCard("Kontak Orang Tua/Wali") {
                    FormTextField(label = "Nama Orang Tua/Wali", value = namaOrtuWali, onValueChange = { namaOrtuWali = it }, keyboardType = KeyboardType.Text)
                    Spacer(modifier = Modifier.height(16.dp))
                    FormTextField(label = "Nomor WhatsApp", value = nomorWaOrtuWali, onValueChange = { nomorWaOrtuWali = it }, placeholder = "Contoh: 081234567890", keyboardType = KeyboardType.Phone)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            Surface(
                modifier = Modifier.fillMaxWidth().height(80.dp).background(Color.White),
                shadowElevation = 8.dp
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 10.dp), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            val updatedProfile = currentProfile.copy(
                                namaLengkap = namaLengkap,
                                jenisKelamin = jenisKelamin,
                                tanggalLahir = tanggalLahir,
                                agama = agama,
                                nomorWhatsApp = nomorWhatsApp,
                                namaOrtuWali = namaOrtuWali,
                                nomorWaOrtuWali = nomorWaOrtuWali
                            )
                            onSaveProfile(updatedProfile)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Simpan", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

// --- Komponen Pembantu ---

@Composable
fun EditFormCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryColor)
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
    // trailingIcon dibuat opsional
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
            trailingIcon = trailingIcon, // Digunakan di sini
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            readOnly = readOnly,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = InputBorderColor,
                cursorColor = PrimaryColor
            )
        )
    }
}

// BottomNavigationBar (Digunakan juga di JadwalScreen)
@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val primaryColor = Color(0xFF2C8AA4)
    val unselectedColor = Color.Gray

    Surface(
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.height(70.dp)
        ) {
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = onNavigateToHome,
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Beranda", modifier = Modifier.size(24.dp)) },
                label = { Text(text = "Beranda", fontSize = 12.sp, fontWeight = if (selectedItem == 0) FontWeight.SemiBold else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryColor, selectedTextColor = primaryColor, unselectedIconColor = unselectedColor, unselectedTextColor = unselectedColor, indicatorColor = primaryColor.copy(alpha = 0.1f))
            )

            NavigationBarItem(
                selected = selectedItem == 1,
                onClick = onNavigateToJadwal,
                icon = { Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Jadwal", modifier = Modifier.size(24.dp)) },
                label = { Text(text = "Jadwal", fontSize = 12.sp, fontWeight = if (selectedItem == 1) FontWeight.SemiBold else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryColor, selectedTextColor = primaryColor, unselectedIconColor = unselectedColor, unselectedTextColor = unselectedColor, indicatorColor = primaryColor.copy(alpha = 0.1f))
            )

            NavigationBarItem(
                selected = selectedItem == 2,
                onClick = onNavigateToProfile,
                icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profil", modifier = Modifier.size(24.dp)) },
                label = { Text(text = "Profil", fontSize = 12.sp, fontWeight = if (selectedItem == 2) FontWeight.SemiBold else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryColor, selectedTextColor = primaryColor, unselectedIconColor = unselectedColor, unselectedTextColor = unselectedColor, indicatorColor = primaryColor.copy(alpha = 0.1f))
            )
        }
    }
}