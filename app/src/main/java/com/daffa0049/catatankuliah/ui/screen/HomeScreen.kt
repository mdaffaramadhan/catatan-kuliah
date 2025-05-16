package com.daffa0049.catatankuliah.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.daffa0049.catatankuliah.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catatan Kuliah") },
                actions = {
                    IconButton(onClick = { viewModel.fetchNotes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                errorMessage != null -> Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> NoteList(notes)
            }
        }
    }
}

@Composable
fun NoteList(notes: List<Note>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(notes) { note ->
            NoteItem(note)
            HorizontalDivider()
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(note.gambar),
            contentDescription = note.judul,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(text = note.judul, style = MaterialTheme.typography.titleLarge)
            Text(
                text = if (note.isi.length > 100) note.isi.take(100) + "..." else note.isi,
                style = MaterialTheme.typography.bodyMedium

            )
        }
    }
}
