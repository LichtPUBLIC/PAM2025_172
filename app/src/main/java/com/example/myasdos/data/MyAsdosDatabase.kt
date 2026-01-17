package com.example.myasdos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myasdos.data.dao.AsdosDao
import com.example.myasdos.data.dao.MahasiswaDao
import com.example.myasdos.data.dao.MatkulDao
import com.example.myasdos.data.entity.Asdos
import com.example.myasdos.data.entity.Mahasiswa
import com.example.myasdos.data.entity.MataKuliah

@Database(
    entities = [Asdos::class, MataKuliah::class, Mahasiswa::class],
    version = 1,
    exportSchema = false
)
abstract class MyAsdosDatabase : RoomDatabase() {
    abstract fun asdosDao(): AsdosDao
    abstract fun matkulDao(): MatkulDao
    abstract fun mahasiswaDao(): MahasiswaDao

    companion object {
        @Volatile
        private var Instance: MyAsdosDatabase? = null

        fun getDatabase(context: Context): MyAsdosDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MyAsdosDatabase::class.java, "myasdos_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}