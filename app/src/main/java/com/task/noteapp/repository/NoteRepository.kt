package com.task.noteapp.repository

import androidx.lifecycle.LiveData
import com.task.noteapp.model.Note
import com.task.noteapp.roomdb.NoteDao
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
    ) : NoteRepositoryInterface {
    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override suspend fun updateNote(
        title: String,
        description: String,
        imageUrl: String,
        editedDate: String,
        noteId: Int
    ) {
        noteDao.updateNote(title,description,imageUrl,editedDate,noteId)
    }

    override fun getSingleNote(noteId: Int): LiveData<Note> {
        return noteDao.getSingleNote(noteId)
    }

    override fun getNotes(): LiveData<List<Note>> {
        return noteDao.observeNotes()
    }
}