package com.daffa0049.catatankuliah.data.model

data class Note(
    val id: String = "",
    val createdAt: String,
    val judul: String,
    val isi: String,
    val gambar: String,
    val user_id: Int
)