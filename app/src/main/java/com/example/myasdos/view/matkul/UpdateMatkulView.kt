package com.example.myasdos.view.matkul

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myasdos.ui.theme.BlueGradientEnd
import com.example.myasdos.ui.theme.BlueGradientStart
import com.example.myasdos.ui.theme.LightBlueBg
import com.example.myasdos.ui.theme.RoyalBlue
import com.example.myasdos.view.ModernCard
import com.example.myasdos.viewmodel.MatkulViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMatkulView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MatkulViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBlueBg)
    ) {
        // 1. BACKGROUND HEADER (Gradient)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BlueGradientStart, BlueGradientEnd)
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        // 2. HEADER CONTENT (Row anti-numpuk)
        Row(
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol Back
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Teks Judul
            Column {
                Text(
                    text = "Edit Kelas", // Judul disesuaikan
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Ubah data jadwal praktikum",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // 3. FLOATING CARD (Formulir)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp) // Posisi kartu menumpuk sedikit di header
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ModernCard(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Edit Data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue
                    )

                    // PENTING: Kita memanggil FormInputMatkul yang ada di InsertMatkulView.kt
                    // Pastikan kedua file ini ada di package yang sama (ui.view.matkul)
                    FormInputMatkul(
                        matkulEvent = viewModel.uiState.matkulEvent,
                        onValueChange = viewModel::updateUiState,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val success = viewModel.updateMatkul() // Panggil fungsi Update di ViewModel
                                if (success) {
                                    Toast.makeText(context, "Update Berhasil", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                    ) {
                        Text("Update Data", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}