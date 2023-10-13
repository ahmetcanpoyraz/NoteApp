package com.task.noteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.model.Note
import com.task.noteapp.repository.NoteRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository : NoteRepositoryInterface
): ViewModel() {
    val noteList = repository.getNotes()

    fun deleteNote(note: Note) = viewModelScope.launch{
        repository.deleteNote(note)
    }
}