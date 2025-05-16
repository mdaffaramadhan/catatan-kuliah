package com.daffa0049.catatankuliah.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.key
import coil.compose.rememberAsyncImagePainter
import com.daffa0049.catatankuliah.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormScreen(
    existingNote: Note? = null,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var judul by remember { mutableStateOf(existingNote?.judul ?: "") }
    var isi by remember { mutableStateOf(existingNote?.isi ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (existingNote == null) "Tambah Catatan" else "Edit Catatan")
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                TextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text("Judul") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = isi,
                    onValueChange = { isi = it },
                    label = { Text("Isi") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Pilih Gambar dari Galeri")
                }
                Spacer(modifier = Modifier.height(8.dp))

                val imageModel = remember(imageUri) {
                    imageUri?.toString() + "?t=${System.currentTimeMillis()}"
                }

                imageUri?.let {
                    key(imageModel) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageModel),
                            contentDescription = "Gambar catatan",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val noteToSave = Note(
                            id = existingNote?.id ?: "",
                            createdAt = existingNote?.createdAt ?: getCurrentTimestamp(),
                            judul = judul,
                            isi = isi,
                            gambar = imageUri?.toString() ?: existingNote?.gambar ?: "",
                            user_id = existingNote?.user_id ?: 1
                        )
                        onSave(noteToSave)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Batal")
                }
            }
        }
    )
}

fun getCurrentTimestamp(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
    return sdf.format(java.util.Date())
}
