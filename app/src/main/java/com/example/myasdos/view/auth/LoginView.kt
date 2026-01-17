package com.example.myasdos.view.auth

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myasdos.R
import com.example.myasdos.ui.theme.RoyalBlue
import com.example.myasdos.viewmodel.AuthViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val context = LocalContext.current // Context untuk Toast
    var showPassword by remember { mutableStateOf(false) }

    // --- LOGIC TOAST & FEEDBACK ---
    if (viewModel.loginSuccess) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
        }
    }
    
    if (viewModel.registerSuccess) {
         LaunchedEffect(Unit) {
            Toast.makeText(context, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()
        }
    }

    // Animasi untuk card entrance
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing), label = ""
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(600, easing = FastOutSlowInEasing), label = ""
    )

    // Background Image
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().scale(1.1f)
        )

        // Form Login Transparan
        Column(
            modifier = Modifier
                .padding(32.dp)
                .widthIn(max = 400.dp)
                .alpha(cardAlpha)
                .scale(cardScale),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon User
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, Modifier.size(32.dp), tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Selamat Datang", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Silakan login untuk melanjutkan", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))

            Spacer(modifier = Modifier.height(32.dp))

            // Input Username
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Username", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Password
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White.copy(alpha = 0.8f))
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
            )

            // --- VALIDATION MESSAGES ---
            Spacer(modifier = Modifier.height(8.dp))
            if (viewModel.emptyFieldError) {
                Text(
                    text = "Harap isi semua data!",
                    color = Color(0xFFFFCDD2),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            if (viewModel.loginError) {
                Text(
                    text = "Username atau Password salah!",
                    color = Color(0xFFFFCDD2),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Login
            Button(
                onClick = { viewModel.login(onLoginSuccess) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White // Solid White
                )
            ) {
                Text(
                    "MASUK",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue // Blue Text
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Belum punya akun?", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                TextButton(onClick = onRegisterClick) {
                    Text("Daftar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}