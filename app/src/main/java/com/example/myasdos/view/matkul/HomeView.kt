package com.example.myasdos.view.matkul

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myasdos.data.entity.MataKuliah
import com.example.myasdos.ui.theme.*
import com.example.myasdos.view.ModernCard
import com.example.myasdos.view.TopSectionHeader
import com.example.myasdos.viewmodel.HomeViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeView(
    onDetailClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val matkulList by viewModel.homeUiState.collectAsState()
    val asdosProfile by viewModel.asdosState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // State untuk Dialog Logout
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = LightBlueBg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = RoyalBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Matkul", modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            // 1. Header Keren
            Box {
                TopSectionHeader(
                    title = "Halo, ${asdosProfile?.namaLengkap ?: "Asdos"}!",
                    subtitle = "Kelola kelas praktikummu hari ini."
                )

                // Tombol Logout (Memicu Dialog)
                IconButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 40.dp, end = 16.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                }
            }

            // 2. Konten List Matkul
            if (matkulList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Class,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Belum ada kelas", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp), // Padding bawah ekstra untuk FAB
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(matkulList, key = { it.idMatkul }) { matkul ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                        ) {
                            MatkulCard(
                                matkul = matkul,
                                onClick = { onDetailClick(matkul.idMatkul) },
                                onEdit = { onEditClick(matkul.idMatkul) },
                                onDelete = {
                                    scope.launch {
                                        viewModel.deleteMatkul(matkul)
                                        Toast.makeText(context, "Kelas berhasil dihapus", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- POPUP KONFIRMASI LOGOUT ---
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = Color.White,
                title = {
                    Text("Konfirmasi Logout", fontWeight = FontWeight.Bold, color = TextBlack)
                },
                text = {
                    Text("Apakah Anda yakin ingin keluar dari aplikasi?", color = TextGray)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout() // Panggil fungsi logout yang asli
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Ya, Keluar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Batal", color = TextGray)
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

// ... (MatkulCard tetap sama seperti sebelumnya) ...
@Composable
fun MatkulCard(
    matkul: MataKuliah,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    ModernCard(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(RoyalBlue.copy(alpha = 0.1f)), // Background Biru Transparan Halus
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Class, // Atau ganti Icons.Default.MenuBook
                    contentDescription = "Ikon Kelas",
                    tint = RoyalBlue, // Warna Ikon Biru Utama
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = matkul.namaMatkul,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${matkul.hari}, ${matkul.jam}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Kelas: ${matkul.kelas}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = RoyalBlue, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = ErrorRed, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color.White,
            title = { Text("Hapus Kelas?", fontWeight = FontWeight.Bold, color = TextBlack) },
            text = { Text("Data mahasiswa di kelas ini juga akan terhapus permanen.", color = TextGray) },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal", color = TextGray) }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}
