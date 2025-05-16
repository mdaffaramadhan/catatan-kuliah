package com.daffa0049.catatankuliah.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daffa0049.catatankuliah.data.network.NoteRepository
import com.daffa0049.catatankuliah.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = NoteRepository(RetrofitInstance.api)

    private val _notes = MutableStateFlow<List<com.daffa0049.catatankuliah.data.model.Note>>(emptyList())
    val notes: StateFlow<List<com.daffa0049.catatankuliah.data.model.Note>> = _notes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _notes.value = repository.getNotes()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
