package com.example.myasdos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myasdos.data.entity.Mahasiswa
import com.example.myasdos.data.entity.MataKuliah
import com.example.myasdos.data.repository.RepositoryMhs
import com.example.myasdos.navigation.DestinasiDetail
import com.example.myasdos.navigation.DestinasiInsertNilai
import com.example.myasdos.navigation.DestinasiUpdateNilai
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MahasiswaViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RepositoryMhs
) : ViewModel() {

    // --- LOGIKA DETAIL KELAS ---
    private val idMatkul: Int? = savedStateHandle.get<Int>(DestinasiDetail.idMatkulArg)
        ?: savedStateHandle.get<String>(DestinasiInsertNilai.idMatkulArg)?.toIntOrNull()
        ?: savedStateHandle.get<Int>(DestinasiInsertNilai.idMatkulArg)

    val matkulDetail: StateFlow<MataKuliah?> = if (idMatkul != null) {
        repository.getMatkul(idMatkul)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    } else {
        MutableStateFlow(null)
    }

    val mahasiswaList: StateFlow<List<Mahasiswa>> = if (idMatkul != null) {
        repository.getMhsByMatkul(idMatkul)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    } else {
        MutableStateFlow(emptyList<Mahasiswa>())
    }

    // --- LOGIKA FORM INSERT/UPDATE ---
    var uiStateMhs by mutableStateOf(MhsUiState())
        private set

    private val idMhsEdit: Int? = savedStateHandle.get<Int>(DestinasiUpdateNilai.idMhsArg)

    init {
        if (idMhsEdit != null) {
            viewModelScope.launch {
                val mhs = repository.getMhsDetail(idMhsEdit)
                    .filterNotNull()
                    .first()
                uiStateMhs = mhs.toUiStateMhs()
            }
        }
    }

    fun updateUiStateMhs(mhsEvent: MhsEvent) {
        uiStateMhs = MhsUiState(mhsEvent = mhsEvent)
    }

    suspend fun saveMhs(): Boolean {
        if (validateInput()) {
            repository.insertMhs(
                uiStateMhs.mhsEvent.toMahasiswa().copy(idMatkul = idMatkul ?: 0)
            )
            return true
        }
        return false
    }

    suspend fun updateMhs(): Boolean {
        if (validateInput()) {
            repository.updateMhs(
                uiStateMhs.mhsEvent.toMahasiswa().copy(idMhs = idMhsEdit ?: 0)
            )
            return true
        }
        return false
    }

    suspend fun deleteMhs(mhs: Mahasiswa) {
        repository.deleteMhs(mhs)
    }

    private fun validateInput(event: MhsEvent = uiStateMhs.mhsEvent): Boolean {
        return event.namaMhs.isNotBlank() && event.nim.isNotBlank() && event.nilai.isNotBlank()
    }
}

// --- STATE & EVENT CLASSES ---

data class MhsUiState(
    val mhsEvent: MhsEvent = MhsEvent(),
    val isEntryValid: Boolean = false
)

data class MhsEvent(
    val idMhs: Int = 0,
    val idMatkul: Int = 0,
    val namaMhs: String = "",
    val nim: String = "",
    val nilai: String = ""
)

fun MhsEvent.toMahasiswa(): Mahasiswa = Mahasiswa(
    idMhs = idMhs,
    idMatkul = idMatkul,
    namaMhs = namaMhs,
    nim = nim,
    nilai = nilai.toDoubleOrNull() ?: 0.0
)

fun Mahasiswa.toUiStateMhs(): MhsUiState = MhsUiState(
    mhsEvent = MhsEvent(
        idMhs = idMhs,
        idMatkul = idMatkul,
        namaMhs = namaMhs,
        nim = nim,
        nilai = nilai.toString()
    )
)