package com.example.myasdos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myasdos.data.entity.Asdos
import com.example.myasdos.data.repository.RepositoryMhs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: RepositoryMhs) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var namaLengkap by mutableStateOf("")

    var loginError by mutableStateOf(false)
    var emptyFieldError by mutableStateOf(false) // Validasi kosong
    var registerSuccess by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false) // Trigger Popup Success

    fun login(onSuccess: () -> Unit) {
        // Reset Error
        loginError = false
        emptyFieldError = false

        // Validasi Kosong
        if (username.isBlank() || password.isBlank()) {
            emptyFieldError = true
            return
        }

        viewModelScope.launch {
            val asdosData = repository.getAsdosStream(username).firstOrNull()
            if (asdosData != null && asdosData.password == password) {
                loginSuccess = true // Trigger Popup
                delay(1000) // Tahan sebentar biar popup muncul
                onSuccess()
                loginSuccess = false // Reset
            } else {
                loginError = true
            }
        }
    }

    fun register(onSuccess: () -> Unit) {
        emptyFieldError = false
        if (username.isBlank() || password.isBlank() || namaLengkap.isBlank()) {
             emptyFieldError = true
             return
        }

        viewModelScope.launch {
            try {
                repository.insertAsdos(Asdos(username, password, namaLengkap))
                registerSuccess = true
                delay(1000)
                onSuccess()
                registerSuccess = false
            } catch (e: Exception) {
                loginError = true
            }
        }
    }
}