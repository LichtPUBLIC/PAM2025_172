package com.example.myasdos.view.matkul

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myasdos.ui.theme.BlueGradientEnd
import com.example.myasdos.ui.theme.BlueGradientStart
import com.example.myasdos.ui.theme.LightBlueBg
import com.example.myasdos.ui.theme.RoyalBlue
import com.example.myasdos.ui.theme.TextGray
import com.example.myasdos.view.ModernCard
import com.example.myasdos.viewmodel.MatkulEvent
import com.example.myasdos.viewmodel.MatkulViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMatkulView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
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
        // 1. BACKGROUND HEADER (Gradient Only)
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

        // 2. HEADER CONTENT (Tombol Back & Judul dalam Satu Row)
        // Menggunakan Row menjamin mereka TIDAK AKAN MENUMPUK
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
                    text = "Tambah Kelas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Buat jadwal praktikum baru",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // 3. FORMULIR DALAM KARTU (Melayang)
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
                        text = "Formulir Data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue
                    )

                    FormInputMatkul(
                        matkulEvent = viewModel.uiState.matkulEvent,
                        onValueChange = viewModel::updateUiState,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val success = viewModel.saveMatkul()
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

// ... imports tetap sama ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputMatkul(
    matkulEvent: MatkulEvent,
    onValueChange: (MatkulEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedKelas by remember { mutableStateOf(false) }
    val kelasOptions = listOf("A", "B", "C", "D", "E")

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

    val dateFormatter = remember { SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID")) }

    val clickableTextFieldColors = OutlinedTextFieldDefaults.colors(
        // Warna saat normal/fokus
        focusedBorderColor = RoyalBlue,
        focusedLabelColor = RoyalBlue,
        cursorColor = RoyalBlue,
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White,

        // Warna saat 'enabled = false' (Agar tidak terlihat abu-abu mati)
        disabledContainerColor = Color.White,
        disabledBorderColor = Color.Gray,      // Tetap ada border
        disabledTextColor = Color.Black,       // Teks tetap hitam pekat
        disabledLabelColor = Color.Gray,       // Label tetap terbaca
        disabledLeadingIconColor = RoyalBlue,  // Ikon tetap biru
        disabledTrailingIconColor = RoyalBlue,
        disabledPlaceholderColor = Color.Gray
    )

    // Warna untuk Text Field biasa (yang bisa diketik)
    val normalTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = RoyalBlue,
        focusedLabelColor = RoyalBlue,
        cursorColor = RoyalBlue,
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. Nama Mata Kuliah (Normal, bisa diketik)
        OutlinedTextField(
            value = matkulEvent.namaMatkul,
            onValueChange = { onValueChange(matkulEvent.copy(namaMatkul = it)) },
            label = { Text("Nama Mata Kuliah") },
            placeholder = { Text("Contoh: PABD") },
            leadingIcon = { Icon(Icons.Default.Class, contentDescription = null, tint = RoyalBlue) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = normalTextFieldColors
        )

        // 2. Dosen Pengampu (Normal, bisa diketik)
        OutlinedTextField(
            value = matkulEvent.dosenPengampu,
            onValueChange = { onValueChange(matkulEvent.copy(dosenPengampu = it)) },
            label = { Text("Dosen Pengampu") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RoyalBlue) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = normalTextFieldColors
        )

        // 3. Kelas Dropdown (Pakai clickableTextFieldColors)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = matkulEvent.kelas,
                onValueChange = {},
                readOnly = true, // ReadOnly agar tidak muncul keyboard
                label = { Text("Pilih Kelas") },
                leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = RoyalBlue) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKelas) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedKelas = true }, // Klik memicu dropdown
                shape = RoundedCornerShape(12.dp),
                colors = clickableTextFieldColors, // <--- WARNA BARU
                enabled = false // Disable agar klik ditangkap oleh Modifier.clickable di atasnya
            )
            DropdownMenu(
                expanded = expandedKelas,
                onDismissRequest = { expandedKelas = false },
                modifier = Modifier.background(Color.White)
            ) {
                kelasOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text("Kelas $option") },
                        onClick = {
                            onValueChange(matkulEvent.copy(kelas = option))
                            expandedKelas = false
                        }
                    )
                }
            }
        }

        // Row Hari & Jam (Pakai clickableTextFieldColors)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // 4. Date Picker
            OutlinedTextField(
                value = matkulEvent.hari,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hari") },
                leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = RoyalBlue) },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(12.dp),
                colors = clickableTextFieldColors, // <--- WARNA BARU
                enabled = false
            )

            // 5. Time Picker
            OutlinedTextField(
                value = matkulEvent.jam,
                onValueChange = {},
                readOnly = true,
                label = { Text("Jam") },
                leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = RoyalBlue) },
                modifier = Modifier
                    .weight(0.8f)
                    .clickable { showTimePicker = true },
                shape = RoundedCornerShape(12.dp),
                colors = clickableTextFieldColors, // <--- WARNA BARU
                enabled = false
            )
        }
    }

    // --- DIALOGS (Tetap Sama) ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val date = Date(selectedDateMillis)
                            onValueChange(matkulEvent.copy(hari = dateFormatter.format(date)))
                        }
                        showDatePicker = false
                    }
                ) { Text("OK", color = RoyalBlue) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal", color = TextGray) }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onConfirm = {
                val hour = timePickerState.hour
                val minute = timePickerState.minute
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                onValueChange(matkulEvent.copy(jam = formattedTime))
                showTimePicker = false
            }
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.White,
                    selectorColor = RoyalBlue,
                    containerColor = Color.White
                )
            )
        }
    }
}

// Dialog Jam Custom
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = { TextButton(onClick = onConfirm) { Text("OK", color = RoyalBlue) } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Batal", color = TextGray) } },
        text = { content() },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}