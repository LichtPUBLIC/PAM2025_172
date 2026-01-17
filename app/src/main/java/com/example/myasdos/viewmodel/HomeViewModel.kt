package com.example.myasdos.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myasdos.data.entity.Asdos
import com.example.myasdos.data.entity.MataKuliah
import com.example.myasdos.data.repository.RepositoryMhs
import com.example.myasdos.navigation.DestinasiHome
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RepositoryMhs
) : ViewModel() {

    // 1. Tangkap Username dari Navigasi
    private val username: String = checkNotNull(savedStateHandle[DestinasiHome.usernameArg])

    // 2. Ambil Data List Matkul
    val homeUiState: StateFlow<List<MataKuliah>> = repository.getAllMatkul()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    // 3. Ambil Data Profil Asdos
    val asdosState: StateFlow<Asdos?> = repository.getAsdosStream(username)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    // Fitur Hapus Matkul (Suspend agar UI bisa menunggu)
    suspend fun deleteMatkul(matkul: MataKuliah) {
        repository.deleteMatkul(matkul)
    }
}