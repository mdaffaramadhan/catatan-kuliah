package com.daffa0049.catatankuliah.data.network

import com.daffa0049.catatankuliah.data.model.Note

class NoteRepository(private val api: ApiService) {

    suspend fun getNotes(): List<Note> {
        return api.getNotes()
    }

    suspend fun addNote(note: Note): Note {
        return api.createNote(note) // ← pakai createNote sesuai ApiService
    }

    suspend fun updateNote(id: String, note: Note): Note {
        return api.updateNote(id, note)
    }

    suspend fun deleteNote(id: String) {
        api.deleteNote(id)
    }
}
