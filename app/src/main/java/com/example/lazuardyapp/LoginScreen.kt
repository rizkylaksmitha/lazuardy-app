package com.example.lazuardyapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.R
import com.example.lazuardyapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    // 1. STATE DITARIK DARI VIEW MODEL
    val email = viewModel.email
    val password = viewModel.password
    val rememberMe = viewModel.rememberMe
    val isLoading = viewModel.isLoading
    val loginStatusMessage = viewModel.loginStatusMessage

    var passwordVisible by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF3892A4)
    val secondaryTextColor = Color(0xFF6B7280)
    val buttonColor = Color(0xFF5B98A8)
    val inputBorderColor = Color(0xFFC7D0D8)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginStatusMessage) {
        if (loginStatusMessage.isNotEmpty() && !loginStatusMessage.contains("Berhasil")) {
            snackbarHostState.showSnackbar(loginStatusMessage)
            viewModel.loginStatusMessage = ""
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Masuk",
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
                    text = "Belum punya akun?",
                    fontSize = 14.sp,
                    color = secondaryTextColor
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Daftar disini",
                        fontSize = 14.sp,
                        color = secondaryTextColor,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Email",
                fontSize = 14.sp,
                color = secondaryTextColor,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Email Anda", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = secondaryTextColor
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = inputBorderColor,
                    unfocusedLeadingIconColor = secondaryTextColor,
                    focusedLeadingIconColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Kata Sandi",
                fontSize = 14.sp,
                color = secondaryTextColor,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Kata sandi Anda", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        tint = secondaryTextColor
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility",
                            tint = secondaryTextColor
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = inputBorderColor,
                    unfocusedLeadingIconColor = secondaryTextColor,
                    focusedLeadingIconColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { viewModel.rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = primaryColor,
                            uncheckedColor = inputBorderColor
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ingat saya",
                        fontSize = 14.sp,
                        color = secondaryTextColor
                    )
                }
            }

            if (loginStatusMessage.isNotEmpty() && !isLoading && loginStatusMessage.contains("Berhasil")) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = loginStatusMessage,
                    color = Color.Green.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.performLogin(onNavigateToDashboard)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = "Masuk",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            )
        }
    }
}