package com.example.myasdos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matkul")
data class MataKuliah(
    @PrimaryKey(autoGenerate = true)
    val idMatkul: Int = 0,
    val namaMatkul: String,
    val dosenPengampu: String,
    val kelas: String,
    val hari: String,
    val jam: String
)