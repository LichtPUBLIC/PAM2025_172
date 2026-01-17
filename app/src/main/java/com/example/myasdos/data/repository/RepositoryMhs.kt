package com.example.myasdos.data.repository

import com.example.myasdos.data.dao.AsdosDao
import com.example.myasdos.data.dao.MahasiswaDao
import com.example.myasdos.data.dao.MatkulDao
import com.example.myasdos.data.entity.Asdos
import com.example.myasdos.data.entity.Mahasiswa
import com.example.myasdos.data.entity.MataKuliah
import kotlinx.coroutines.flow.Flow

class RepositoryMhs(
    private val asdosDao: AsdosDao,
    private val matkulDao: MatkulDao,
    private val mahasiswaDao: MahasiswaDao
) {
    // Asdos Ops
    suspend fun insertAsdos(asdos: Asdos) = asdosDao.insertAsdos(asdos)
    suspend fun getAsdos(username: String) = asdosDao.getAsdos(username)

    // Matkul Ops
    suspend fun insertMatkul(mk: MataKuliah) = matkulDao.insertMatkul(mk)
    suspend fun updateMatkul(mk: MataKuliah) = matkulDao.updateMatkul(mk)
    suspend fun deleteMatkul(mk: MataKuliah) = matkulDao.deleteMatkul(mk)
    fun getAllMatkul(): Flow<List<MataKuliah>> = matkulDao.getAllMatkul()
    fun getMatkul(id: Int): Flow<MataKuliah> = matkulDao.getMatkul(id)

    // Mahasiswa Ops
    suspend fun insertMhs(mhs: Mahasiswa) = mahasiswaDao.insertMahasiswa(mhs)
    suspend fun updateMhs(mhs: Mahasiswa) = mahasiswaDao.updateMahasiswa(mhs)
    suspend fun deleteMhs(mhs: Mahasiswa) = mahasiswaDao.deleteMahasiswa(mhs)
    fun getMhsByMatkul(idMatkul: Int): Flow<List<Mahasiswa>> = mahasiswaDao.getMhsByMatkul(idMatkul)
    fun searchMhs(idMatkul: Int, query: String): Flow<List<Mahasiswa>> = mahasiswaDao.searchMhs(idMatkul, query)
    fun getMhsDetail(id: Int): Flow<Mahasiswa> = mahasiswaDao.getMhsDetail(id)
    fun getAsdosStream(username: String): Flow<Asdos?> = asdosDao.getAsdos(username)
}