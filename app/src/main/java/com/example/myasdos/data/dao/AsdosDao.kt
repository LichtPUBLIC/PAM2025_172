package com.example.myasdos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myasdos.data.entity.Asdos
import kotlinx.coroutines.flow.Flow

@Dao
interface AsdosDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsdos(asdos: Asdos)

    @Query("SELECT * FROM asdos WHERE username = :username")
    fun getAsdos(username: String): Flow<Asdos?>



}