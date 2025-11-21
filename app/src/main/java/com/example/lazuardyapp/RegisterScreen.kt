package com.example.lazuardyapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val primaryColor = Color(0xFF3892A4)
    val secondaryTextColor = Color(0xFF6B7280)
    val buttonColor = Color(0xFF5B98A8)
    val inputBorderColor = Color(0xFFC7D0D8)
    val backgroundColor = Color(0xFFFFFFFF)

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val screenHorizontalPadding = 32.dp

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateRegister(): Boolean {
        var isValid = true
        if (email.isEmpty()) { emailError = "Email tidak boleh kosong"; isValid = false }
        else if (!isValidEmail(email)) { emailError = "Format email tidak valid"; isValid = false }
        else { emailError = null }

        if (password.isEmpty()) { passwordError = "Password tidak boleh kosong"; isValid = false }
        else if (password.length < 8) { passwordError = "Password minimal 8 karakter"; isValid = false }
        else { passwordError = null }

        if (confirmPassword.isEmpty()) { confirmPasswordError = "Konfirmasi password tidak boleh kosong"; isValid = false }
        else if (confirmPassword != password) { confirmPasswordError = "Password tidak cocok"; isValid = false }
        else { confirmPasswordError = null }
        return isValid
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.padding(top = 16.dp, start = screenHorizontalPadding - 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = secondaryTextColor
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = backgroundColor,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = screenHorizontalPadding)
                        .padding(bottom = 88.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Daftar sebagai siswa",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sudah punya akun?",
                            fontSize = 14.sp,
                            color = secondaryTextColor
                        )
                        TextButton(
                            onClick = onNavigateToLogin,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Masuk",
                                fontSize = 14.sp,
                                color = secondaryTextColor,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Email",
                        fontSize = 14.sp,
                        color = secondaryTextColor,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email Anda", color = Color.Gray) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = secondaryTextColor) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryColor, unfocusedBorderColor = inputBorderColor, unfocusedLeadingIconColor = secondaryTextColor, focusedLeadingIconColor = primaryColor, cursorColor = primaryColor)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(text = "Kata Sandi", fontSize = 14.sp, color = secondaryTextColor, fontWeight = FontWeight.Medium)
                        Text(text = "Kata sandi minimal 8 karakter", fontSize = 12.sp, color = secondaryTextColor)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Kata sandi Anda", color = Color.Gray) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon", tint = secondaryTextColor) },
                        trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "Toggle Password Visibility", tint = secondaryTextColor) } },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryColor, unfocusedBorderColor = inputBorderColor, unfocusedLeadingIconColor = secondaryTextColor, focusedLeadingIconColor = primaryColor, cursorColor = primaryColor)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Konfirmasi Kata Sandi", fontSize = 14.sp, color = secondaryTextColor, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Konfirmasi kata sandi Anda", color = Color.Gray) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password Icon", tint = secondaryTextColor) },
                        trailingIcon = { IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) { Icon(imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "Toggle Password Visibility", tint = secondaryTextColor) } },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryColor, unfocusedBorderColor = inputBorderColor, unfocusedLeadingIconColor = secondaryTextColor, focusedLeadingIconColor = primaryColor, cursorColor = primaryColor)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, colors = CheckboxDefaults.colors(checkedColor = primaryColor, uncheckedColor = inputBorderColor), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Ingat saya", fontSize = 14.sp, color = secondaryTextColor)
                    }
                }

                Button(
                    onClick = onRegisterSuccess,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = screenHorizontalPadding),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Daftar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 88.dp)
                )
            }
        }
    )
}