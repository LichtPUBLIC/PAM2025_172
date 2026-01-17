package com.example.myasdos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mahasiswa",
    foreignKeys = [
        ForeignKey(
            entity = MataKuliah::class,
            parentColumns = ["idMatkul"],
            childColumns = ["idMatkul"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Mahasiswa(
    @PrimaryKey(autoGenerate = true)
    val idMhs: Int = 0,
    val idMatkul: Int, // FK
    val namaMhs: String,
    val nim: String,
    val nilai: Double
)