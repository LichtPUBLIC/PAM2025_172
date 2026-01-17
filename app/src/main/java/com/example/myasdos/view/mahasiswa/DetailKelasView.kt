package com.example.myasdos.view.mahasiswa

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myasdos.data.entity.Mahasiswa
import com.example.myasdos.ui.theme.*
import com.example.myasdos.view.ModernCard
import com.example.myasdos.viewmodel.MahasiswaViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailKelasView(
    onBackClick: () -> Unit,
    onAddNilaiClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: MahasiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val matkul by viewModel.matkulDetail.collectAsState()
    val mhsList by viewModel.mahasiswaList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        containerColor = LightBlueBg,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNilaiClick,
                containerColor = RoyalBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tambah Mahasiswa")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // --- HEADER YANG DIPERBAIKI (MENGGUNAKAN ROW) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Tinggi Header
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(BlueGradientStart, BlueGradientEnd)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
            ) {
                // Gunakan Row agar Tombol Back dan Teks TIDAK BERTUMPUK
                Row(
                    modifier = Modifier
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp) // Padding atas untuk Status Bar
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Tombol Back
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), CircleShape) // Background transparan
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 2. Teks Judul (Nama Matkul)
                    Column {
                        Text(
                            text = matkul?.namaMatkul ?: "Detail Kelas",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1 // Biar tidak turun baris berlebihan
                        )
                        Text(
                            text = matkul?.dosenPengampu ?: "Memuat...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // --- INFO CARD MELAYANG (Jadwal & Total) ---
            ModernCard(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp) // Tarik ke atas menimpa header
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Kiri: Jadwal
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(LightBlueBg, CircleShape),
                                contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = RoyalBlue)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Jadwal", fontSize = 12.sp, color = TextGray)
                            Text("${matkul?.hari}, ${matkul?.jam}", fontWeight = FontWeight.Bold, color = TextBlack)
                        }
                    }

                    // Garis Pemisah Vertical
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color.LightGray)
                    )

                    // Kanan: Total Mhs
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Total Mhs", fontSize = 12.sp, color = TextGray)
                        Text("${mhsList.size} Orang", fontWeight = FontWeight.Bold, color = RoyalBlue, fontSize = 16.sp)
                    }
                }
            }

            // --- LIST MAHASISWA ---
            if (mhsList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().offset(y = (-40).dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.GroupOff, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Belum ada mahasiswa", color = TextGray)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp), // Padding bawah ekstra untuk FAB
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.offset(y = (-20).dp)
                ) {
                    items(mhsList) { mhs ->
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInHorizontally()
                        ) {
                            MahasiswaItemCard(
                                mhs = mhs,
                                onEdit = onEditClick,
                                onDelete = {
                                    coroutineScope.launch {
                                        viewModel.deleteMhs(mhs)
                                        Toast.makeText(context, "Mahasiswa berhasil dihapus", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MahasiswaItemCard(mhs: Mahasiswa, onEdit: (Int) -> Unit, onDelete: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    // Logika Warna Badge Nilai
    val (badgeColor, textColor) = when {
        mhs.nilai >= 85 -> Pair(Color(0xFFDCFCE7), Color(0xFF166534)) // Hijau (A)
        mhs.nilai >= 70 -> Pair(Color(0xFFDBEAFE), Color(0xFF1E40AF)) // Biru (B)
        mhs.nilai >= 50 -> Pair(Color(0xFFFEF9C3), Color(0xFF854D0E)) // Kuning (C)
        else -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B))            // Merah (D/E)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Shadow halus
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6)) // Border tipis biar rapi
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Badge Nilai (Lingkaran)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
                    .border(2.dp, Color.White, CircleShape), // Ring putih di dalam
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (mhs.nilai % 1.0 == 0.0) mhs.nilai.toInt().toString() else mhs.nilai.toString(),
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Info Nama & NIM
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mhs.namaMhs,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = mhs.nim,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }

            // 3. Tombol Aksi (Edit & Hapus)
            Row {
                IconButton(
                    onClick = { onEdit(mhs.idMhs) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = RoyalBlue, modifier = Modifier.size(20.dp))
                }
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
        }
    }

    // Dialog Konfirmasi Hapus
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White,
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed) },
            title = { Text("Hapus Mahasiswa?", fontWeight = FontWeight.Bold, color = TextBlack) },
            text = { Text("Data ${mhs.namaMhs} akan dihapus permanen.", color = TextGray, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Batal", color = TextGray) }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
