package com.example.myasdos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myasdos.data.entity.MataKuliah
import kotlinx.coroutines.flow.Flow

@Dao
interface MatkulDao {
    @Insert
    suspend fun insertMatkul(mataKuliah: MataKuliah)

    @Update
    suspend fun updateMatkul(mataKuliah: MataKuliah)

    @Delete
    suspend fun deleteMatkul(mataKuliah: MataKuliah)

    @Query("SELECT * FROM matkul ORDER BY namaMatkul ASC")
    fun getAllMatkul(): Flow<List<MataKuliah>>

    @Query("SELECT * FROM matkul WHERE idMatkul = :id")
    fun getMatkul(id: Int): Flow<MataKuliah>
}