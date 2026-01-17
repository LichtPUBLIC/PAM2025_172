package com.example.myasdos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myasdos.data.entity.Mahasiswa
import kotlinx.coroutines.flow.Flow

@Dao
interface MahasiswaDao {
    @Insert
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)

    @Update
    suspend fun updateMahasiswa(mahasiswa: Mahasiswa)

    @Delete
    suspend fun deleteMahasiswa(mahasiswa: Mahasiswa)

    // Ambil Mhs berdasarkan Kelas
    @Query("SELECT * FROM mahasiswa WHERE idMatkul = :idMatkul ORDER BY namaMhs ASC")
    fun getMhsByMatkul(idMatkul: Int): Flow<List<Mahasiswa>>

    // Fitur Search SRS
    @Query("SELECT * FROM mahasiswa WHERE idMatkul = :idMatkul AND (namaMhs LIKE '%' || :query || '%' OR nim LIKE '%' || :query || '%')")
    fun searchMhs(idMatkul: Int, query: String): Flow<List<Mahasiswa>>

    @Query("SELECT * FROM mahasiswa WHERE idMhs = :id")
    fun getMhsDetail(id: Int): Flow<Mahasiswa>
}