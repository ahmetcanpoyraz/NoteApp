package com.task.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.noteapp.model.Note

class FakeNoteRepository : NoteRepositoryInterface {

    private val notes = mutableListOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notes)
    private val singleNote = MutableLiveData<Note>()

    override suspend fun insertNote(note: Note) {
        notes.add(note)
        refreshData()
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
        refreshData()
    }

    override suspend fun updateNote(
        title: String,
        description: String,
        imageUrl: String,
        editedDate: String,
        noteId: Int
    ) {
        notes.find {it.id == noteId}?.title = title
        notes.find {it.id == noteId}?.description = description
        notes.find {it.id == noteId}?.imageUrl = imageUrl
        notes.find {it.id == noteId}?.editedDate = editedDate
        refreshData()
    }

    override fun getSingleNote(noteId: Int): LiveData<Note> {
        return singleNote
    }

    override fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    private fun refreshData(){
        notesLiveData.postValue(notes)
    }
}