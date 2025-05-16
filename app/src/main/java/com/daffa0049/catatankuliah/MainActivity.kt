package com.daffa0049.catatankuliah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.daffa0049.catatankuliah.ui.screen.HomeScreen
import com.daffa0049.catatankuliah.ui.screen.HomeViewModel

class MainActivity : ComponentActivity() {

    private val homeViewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(homeViewModel)

            LaunchedEffect(Unit) {
                homeViewModel.fetchNotes()
            }
        }
    }
}
