package com.example.myasdos.view.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.myasdos.ui.theme.LightBlueBg
import com.example.myasdos.ui.theme.RoyalBlue
import com.example.myasdos.ui.theme.TextGray
import com.example.myasdos.viewmodel.AuthViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterView(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var showPassword by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.registerSuccess) {
        if (viewModel.registerSuccess) {
            showSuccessDialog = true
            delay(2000)
            viewModel.registerSuccess = false
            onRegisterSuccess()
        }
    }

    // Animasi untuk card entrance
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val cardAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "cardAlpha"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "cardScale"
    )

    // Animasi floating untuk background
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingOffset"
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
            modifier = Modifier
                .fillMaxSize()
                .scale(1.1f) // Zoom in sedikit untuk menghilangkan pinggir putih
        )

        // Form Register Transparan - Tanpa Card
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .widthIn(max = 400.dp)
                .alpha(cardAlpha)
                .scale(cardScale),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon Register
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Buat Akun Baru",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Isi data diri Anda di bawah ini",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input Nama Lengkap
            OutlinedTextField(
                value = viewModel.namaLengkap,
                onValueChange = { viewModel.namaLengkap = it },
                label = { Text("Nama Lengkap", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Color.White) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.3f), // Lebih tebal (0.1 -> 0.3)
                        shape = RoundedCornerShape(12.dp)
                    ),
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

            // Input Username
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Username", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.3f), // Lebih tebal (0.1 -> 0.3)
                        shape = RoundedCornerShape(12.dp)
                    ),
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
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.3f), // Lebih tebal (0.1 -> 0.3)
                        shape = RoundedCornerShape(12.dp)
                    ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
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

            // Pesan Error Validasi
            if (viewModel.emptyFieldError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Harap isi semua data!",
                    color = Color(0xFFFFCDD2),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Daftar
            Button(
                onClick = { viewModel.register {} },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White // Solid White
                )
            ) {
                Text(
                    "DAFTAR SEKARANG",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue // Blue Text
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Kembali
            TextButton(onClick = onBackClick) {
                Text("Sudah punya akun? Masuk", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = Color.White,
            title = { Text("Berhasil!", color = RoyalBlue, fontWeight = FontWeight.Bold) },
            text = { Text("Akun Anda berhasil dibuat. Silakan login.", color = TextGray) },
            confirmButton = {},
            shape = RoundedCornerShape(16.dp)
        )
    }
}