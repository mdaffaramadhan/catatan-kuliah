package com.daffa0049.catatankuliah.ui.screen

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.daffa0049.catatankuliah.data.model.Note
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormScreen(
    existingNote: Note? = null,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var judul by remember { mutableStateOf(existingNote?.judul ?: "") }
    var isi by remember { mutableStateOf(existingNote?.isi ?: "") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it }
    }
    val cameraPermission = android.Manifest.permission.CAMERA

    val tempCameraUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = tempCameraUri.value
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = File.createTempFile("temp_image", ".jpg", context.cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            tempCameraUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
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

                Button(onClick = {
                    // Show dialog pilihan
                    val options = listOf("Kamera", "Galeri")
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Pilih sumber gambar")
                    builder.setItems(options.toTypedArray()) { _, which ->
                        when (which) {
                            0 -> {
                                permissionLauncher.launch(cameraPermission)
                            }

                            1 -> galleryLauncher.launch("image/*")
                        }
                    }
                    builder.show()
                }) {
                    Text("Pilih Gambar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                val imageModel = remember(imageUri) {
                    imageUri?.toString() + "?t=${System.currentTimeMillis()}"
                }

                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(150.dp)
                    )
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

fun createImageUri(context: Context): Uri? {
    val imagesDir = context.cacheDir
    val image = File(imagesDir, "camera_photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        image
    )
}


