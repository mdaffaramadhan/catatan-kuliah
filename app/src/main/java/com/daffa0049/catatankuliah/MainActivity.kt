package com.daffa0049.catatankuliah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daffa0049.catatankuliah.ui.screen.HomeScreen
import com.daffa0049.catatankuliah.ui.screen.HomeViewModel
import com.daffa0049.catatankuliah.ui.screen.NoteFormScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val homeViewModel: HomeViewModel = viewModel()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onAddNote = {
                            navController.navigate("form/")
                        },
                        onEditNote = { note ->
                            navController.navigate("form/${note.id}")
                        }
                    )
                    LaunchedEffect(Unit) {
                        homeViewModel.fetchNotes()
                    }
                }
                composable(
                    route = "form/{noteId}",
                    arguments = listOf(navArgument("noteId") {
                        type = NavType.StringType
                        defaultValue = ""
                    })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    val notes by homeViewModel.notes.collectAsState()
                    val note = notes.find { it.id == noteId }

                    NoteFormScreen(
                        existingNote = note,
                        onSave = { updatedNote ->
                            if (noteId.isEmpty()) {
                                homeViewModel.addNote(updatedNote)
                            } else {
                                homeViewModel.updateNote(updatedNote)
                            }
                            navController.popBackStack()
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
