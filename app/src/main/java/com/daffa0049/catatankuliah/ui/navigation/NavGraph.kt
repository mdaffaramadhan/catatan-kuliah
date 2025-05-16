package com.daffa0049.catatankuliah.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.daffa0049.catatankuliah.ui.screen.HomeScreen
import com.daffa0049.catatankuliah.ui.screen.HomeViewModel
import com.daffa0049.catatankuliah.ui.screen.NoteFormScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Form : Screen("form")
}

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: HomeViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddNote = {
                    navController.navigate(Screen.Form.route)
                },
                onEditNote = { /* Nanti bisa pakai parameter jika ingin edit */ }
            )
        }

        composable(Screen.Form.route) {
            NoteFormScreen(
                onSave = { note ->
                    viewModel.addNote(note)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
