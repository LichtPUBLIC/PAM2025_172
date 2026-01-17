package com.example.myasdos.navigation

interface AlamatNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiLogin : AlamatNavigasi {
    override val route = "login"
    override val titleRes = "Login"
}

object DestinasiHome : AlamatNavigasi {
    override val route = "home"
    override val titleRes = "Dashboard"
    const val usernameArg = "username"
    val routeWithArgs = "$route/{$usernameArg}"
}

object DestinasiInsertMatkul : AlamatNavigasi {
    override val route = "insert_matkul"
    override val titleRes = "Tambah Mata Kuliah"
}

object DestinasiDetail : AlamatNavigasi {
    override val route = "detail_kelas"
    override val titleRes = "Detail Kelas"
    const val idMatkulArg = "idMatkul"
    val routeWithArgs = "$route/{$idMatkulArg}"
}

object DestinasiInsertNilai : AlamatNavigasi {
    override val route = "insert_nilai"
    override val titleRes = "Input Nilai"
    const val idMatkulArg = "idMatkul"
    val routeWithArgs = "$route/{$idMatkulArg}"
}

object DestinasiRegister : AlamatNavigasi {
    override val route = "register"
    override val titleRes = "Daftar Akun"
}

object DestinasiUpdateMatkul : AlamatNavigasi {
    override val route = "update_matkul"
    override val titleRes = "Edit Mata Kuliah"
    const val idMatkulArg = "idMatkul"
    val routeWithArgs = "$route/{$idMatkulArg}"
}

object DestinasiUpdateNilai : AlamatNavigasi {
    override val route = "update_nilai"
    override val titleRes = "Edit Nilai"
    const val idMhsArg = "idMhs"
    val routeWithArgs = "$route/{$idMhsArg}"
}