package com.example.myasdos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myasdos.view.auth.LoginView
import com.example.myasdos.view.auth.RegisterView
import com.example.myasdos.view.mahasiswa.DetailKelasView
import com.example.myasdos.view.matkul.HomeView
import com.example.myasdos.view.matkul.InsertMatkulView
import com.example.myasdos.view.matkul.UpdateMatkulView
import com.example.myasdos.view.mahasiswa.InsertNilaiView
import com.example.myasdos.view.mahasiswa.UpdateNilaiView
import com.example.myasdos.viewmodel.AuthViewModel
import com.example.myasdos.viewmodel.PenyediaViewModel

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiLogin.route,
        modifier = Modifier
    ) {
        // 1. LOGIN: Saat sukses, kirim username ke Home
        composable(DestinasiLogin.route) {
            val viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
            LoginView(
                onLoginSuccess = {
                    val username = viewModel.username // Ambil username yang diketik
                    navController.navigate("${DestinasiHome.route}/$username") {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(DestinasiRegister.route)
                }
            )
        }

        // 2. REGISTER: Saat sukses, kirim username ke Home
        composable(DestinasiRegister.route) {
            val viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
            RegisterView(
                onRegisterSuccess = {
                    val username = viewModel.username // Ambil username yang baru didaftarkan
                    navController.navigate("${DestinasiHome.route}/$username") {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 3. HOME: Terima Username dan berikan ke ViewModel
        composable(
            route = DestinasiHome.routeWithArgs,
            arguments = listOf(navArgument(DestinasiHome.usernameArg) { type = NavType.StringType })
        ) {
            HomeView(
                onDetailClick = { idMatkul ->
                    navController.navigate("${DestinasiDetail.route}/$idMatkul")
                },
                onAddClick = {
                    navController.navigate(DestinasiInsertMatkul.route)
                },
                onEditClick = { idMatkul ->
                    navController.navigate("${DestinasiUpdateMatkul.route}/$idMatkul")
                },
                onLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiHome.routeWithArgs) { inclusive = true } // Bersihkan stack
                    }
                }
            )
        }

        // Halaman Detail Kelas
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.idMatkulArg) { type = NavType.IntType })
        ) { backStackEntry ->
            val idMatkul = backStackEntry.arguments?.getInt(DestinasiDetail.idMatkulArg)

            DetailKelasView(
                onBackClick = { navController.popBackStack() },
                onAddNilaiClick = {
                    navController.navigate("${DestinasiInsertNilai.route}/$idMatkul")
                },
                onEditClick = { idMhs ->
                    navController.navigate("${DestinasiUpdateNilai.route}/$idMhs")
                }
            )
        }

        composable(DestinasiInsertMatkul.route) {
            InsertMatkulView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() }
            )
        }

        // 5. Halaman Edit Mata Kuliah (Perhatikan argumennya)
        composable(
            route = DestinasiUpdateMatkul.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateMatkul.idMatkulArg) { type = NavType.IntType })
        ) {
            UpdateMatkulView(
                onBack = { navController.popBackStack() }
            )
        }

        // 6. Halaman Input Nilai
        composable(
            route = DestinasiInsertNilai.routeWithArgs,
            arguments = listOf(navArgument(DestinasiInsertNilai.idMatkulArg) { type = NavType.IntType })
        ) {
            InsertNilaiView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() }
            )
        }

        // 7. Halaman Edit Nilai
        composable(
            route = DestinasiUpdateNilai.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateNilai.idMhsArg) { type = NavType.IntType })
        ) {
            UpdateNilaiView(
                onBack = { navController.popBackStack() }
            )
        }
    }
}