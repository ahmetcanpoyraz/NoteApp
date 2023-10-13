package com.task.noteapp.repository

import androidx.lifecycle.LiveData
import com.task.noteapp.model.Note


interface NoteRepositoryInterface {
    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(title: String,description: String, imageUrl: String, editedDate: String,noteId : Int)

    fun getSingleNote(noteId: Int) : LiveData<Note>

    fun getNotes() : LiveData<List<Note>>

}