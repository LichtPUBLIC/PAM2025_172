package com.example.myasdos.dependencies

import android.content.Context
import com.example.myasdos.data.MyAsdosDatabase
import com.example.myasdos.data.repository.RepositoryMhs

interface AppContainer {
    val repositoryMhs: RepositoryMhs
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val repositoryMhs: RepositoryMhs by lazy {
        val db = MyAsdosDatabase.getDatabase(context)
        RepositoryMhs(db.asdosDao(), db.matkulDao(), db.mahasiswaDao())
    }
}