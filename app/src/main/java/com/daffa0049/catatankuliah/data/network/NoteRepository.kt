package com.daffa0049.catatankuliah.data.network

import com.daffa0049.catatankuliah.data.model.Note

class NoteRepository(private val api: ApiService) {
    suspend fun getNotes(): List<Note> = api.getNotes()
}
