package com.example.myasdos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myasdos.data.entity.MataKuliah
import com.example.myasdos.data.repository.RepositoryMhs
import com.example.myasdos.navigation.DestinasiUpdateMatkul
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MatkulViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RepositoryMhs
) : ViewModel() {

    var uiState by mutableStateOf(MatkulUiState())
        private set

    // Khusus untuk Mode Edit (Ambil ID dari Navigasi)
    private val idMatkul: Int? = savedStateHandle[DestinasiUpdateMatkul.idMatkulArg]

    init {
        if (idMatkul != null) {
            viewModelScope.launch {
                val matkul = repository.getMatkul(idMatkul).filterNotNull().first()
                uiState = matkul.toUiStateMatkul()
            }
        }
    }

    fun updateUiState(matkulEvent: MatkulEvent) {
        uiState = MatkulUiState(matkulEvent = matkulEvent)
    }

    suspend fun saveMatkul(): Boolean {
        if (validateInput()) {
            repository.insertMatkul(uiState.matkulEvent.toMataKuliah())
            return true
        }
        return false
    }

    suspend fun updateMatkul(): Boolean {
        if (validateInput()) {
            repository.updateMatkul(uiState.matkulEvent.toMataKuliah().copy(idMatkul = idMatkul ?: 0))
            return true
        }
        return false
    }

    private fun validateInput(event: MatkulEvent = uiState.matkulEvent): Boolean {
        return event.namaMatkul.isNotBlank() && event.dosenPengampu.isNotBlank() && event.kelas.isNotBlank()
    }
}

// --- STATE & EVENT CLASSES ---

data class MatkulUiState(
    val matkulEvent: MatkulEvent = MatkulEvent(),
    val isEntryValid: Boolean = false
)

data class MatkulEvent(
    val idMatkul: Int = 0,
    val namaMatkul: String = "",
    val dosenPengampu: String = "",
    val kelas: String = "",
    val hari: String = "",
    val jam: String = ""
)

// Extension Functions Konversi Data
fun MatkulEvent.toMataKuliah(): MataKuliah = MataKuliah(
    idMatkul = idMatkul,
    namaMatkul = namaMatkul,
    dosenPengampu = dosenPengampu,
    kelas = kelas,
    hari = hari,
    jam = jam
)

fun MataKuliah.toUiStateMatkul(): MatkulUiState = MatkulUiState(
    matkulEvent = MatkulEvent(
        idMatkul = idMatkul,
        namaMatkul = namaMatkul,
        dosenPengampu = dosenPengampu,
        kelas = kelas,
        hari = hari,
        jam = jam
    )
)
