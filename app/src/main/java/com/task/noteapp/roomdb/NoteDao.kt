package com.task.noteapp.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.task.noteapp.model.Note

@Dao
interface NoteDao {

    // Add note to roomDB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note)

    // Delete note from roomDB
    @Delete
    suspend fun deleteNote(note: Note)

    // Update note
    @Query("UPDATE notes SET title=:title, description=:description, imageUrl=:imageUrl, editedDate=:editedDate WHERE id = :id")
    fun updateNote(title: String, description: String,imageUrl: String, editedDate: String, id: Int)

    // Get All Notes
    @Query("SELECT * FROM notes")
    fun observeNotes() : LiveData<List<Note>>

    // Get Single Note
    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getSingleNote(noteId: Int) : LiveData<Note>
}