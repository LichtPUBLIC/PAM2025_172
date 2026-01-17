package com.example.myasdos.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myasdos.MyAsdosApplication

object PenyediaViewModel {
    val Factory = viewModelFactory {

        // 1. Auth ViewModel (Login/Register)
        initializer {
            AuthViewModel(myAsdosApp().container.repositoryMhs)
        }

        // 2. Home ViewModel (Dashboard Matkul)
        initializer {
            HomeViewModel(
                this.createSavedStateHandle(), // Tambahkan ini
                myAsdosApp().container.repositoryMhs
            )
        }

        // 3. Matkul ViewModel (Tambah/Edit Matkul)
        initializer {
            MatkulViewModel(
                this.createSavedStateHandle(),
                myAsdosApp().container.repositoryMhs
            )
        }

        // 4. Mahasiswa ViewModel (Detail Kelas, Input/Edit Nilai)
        initializer {
            MahasiswaViewModel(
                this.createSavedStateHandle(),
                myAsdosApp().container.repositoryMhs
            )
        }
    }
}

fun CreationExtras.myAsdosApp(): MyAsdosApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyAsdosApplication)