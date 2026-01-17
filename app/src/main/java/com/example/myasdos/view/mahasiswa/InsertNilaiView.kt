package com.example.myasdos.view.mahasiswa

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myasdos.ui.theme.BlueGradientEnd
import com.example.myasdos.ui.theme.BlueGradientStart
import com.example.myasdos.ui.theme.LightBlueBg
import com.example.myasdos.ui.theme.RoyalBlue
import com.example.myasdos.ui.theme.TextGray
import com.example.myasdos.view.ModernCard
import com.example.myasdos.viewmodel.MahasiswaViewModel
import com.example.myasdos.viewmodel.MhsEvent
import com.example.myasdos.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertNilaiView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MahasiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBlueBg)
    ) {
        // 1. HEADER GRADIENT BIRU
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

        // 2. HEADER CONTENT (Row Anti-Numpuk)
        Row(
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Column {
                Text(
                    text = "Input Mahasiswa",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Masukkan data dan nilai praktikum",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // 3. FLOATING CARD (Formulir)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp) // Posisi kartu
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
                        text = "Formulir Nilai",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue
                    )

                    // Panggil Form Input yang Dipercantik
                    FormInputMhs(
                        mhsEvent = viewModel.uiStateMhs.mhsEvent,
                        onValueChange = viewModel::updateUiStateMhs,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val success = viewModel.saveMhs()
                                if (success) {
                                    Toast.makeText(context, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                    onNavigate()
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
                        Text("Simpan Data", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun FormInputMhs(
    mhsEvent: MhsEvent,
    onValueChange: (MhsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Style TextField Konsisten
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = RoyalBlue,
        focusedLabelColor = RoyalBlue,
        cursorColor = RoyalBlue,
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White
    )

    // Logika Helper Stepper
    fun adjustNilai(increment: Boolean) {
        val currentVal = mhsEvent.nilai.toDoubleOrNull() ?: 0.0
        val step = 1.0
        val newVal = if (increment) currentVal + step else currentVal - step
        val clampedVal = newVal.coerceIn(0.0, 100.0)

        val finalString = if (clampedVal % 1.0 == 0.0) {
            clampedVal.toInt().toString()
        } else {
            clampedVal.toString()
        }
        onValueChange(mhsEvent.copy(nilai = finalString))
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. Input Nama
        OutlinedTextField(
            value = mhsEvent.namaMhs,
            onValueChange = { onValueChange(mhsEvent.copy(namaMhs = it)) },
            label = { Text("Nama Mahasiswa") },
            placeholder = { Text("Nama Lengkap") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RoyalBlue) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = textFieldColors
        )

        // 2. Input NIM
        OutlinedTextField(
            value = mhsEvent.nim,
            onValueChange = { onValueChange(mhsEvent.copy(nim = it)) },
            label = { Text("NIM") },
            placeholder = { Text("Nomor Induk") },
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = RoyalBlue) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = textFieldColors
        )

        // 3. Input Nilai dengan Stepper Keren
        Text("Nilai Praktikum", style = MaterialTheme.typography.labelLarge, color = TextGray)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Tombol Kurang (Merah Tipis)
            FilledTonalIconButton(
                onClick = { adjustNilai(false) },
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color(0xFFFEE2E2), // Merah Muda
                    contentColor = Color(0xFFDC2626)    // Merah Tua
                ),
                modifier = Modifier.size(50.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Kurangi")
            }

            // Input Nilai (Tengah)
            OutlinedTextField(
                value = mhsEvent.nilai,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        onValueChange(mhsEvent.copy(nilai = it))
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue
                ),
                colors = textFieldColors
            )

            // Tombol Tambah (Biru Tipis)
            FilledTonalIconButton(
                onClick = { adjustNilai(true) },
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color(0xFFDBEAFE), // Biru Muda
                    contentColor = RoyalBlue
                ),
                modifier = Modifier.size(50.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    }
}