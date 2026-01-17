package com.example.myasdos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asdos")
data class Asdos(
    @PrimaryKey
    val username: String,
    val password: String,
    val namaLengkap: String
)