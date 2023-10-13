package com.task.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.task.noteapp.model.Note
import com.task.noteapp.repository.NoteRepositoryInterface
import com.task.noteapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val repository : NoteRepositoryInterface
): ViewModel() {
    // Check Success or Error
    private var insertNoteMsg = MutableLiveData<Resource<Note>>()
    val insertNoteMessage : LiveData<Resource<Note>>
    get() = insertNoteMsg

    // Check Success or Error
    private var updateNoteMsg = MutableLiveData<Resource<Note>>()
    val updateNoteMessage : LiveData<Resource<Note>>
    get() = updateNoteMsg

    lateinit var singleNote : LiveData<Note>


    fun resetInsertNoteMessage(){
        insertNoteMsg = MutableLiveData<Resource<Note>>()
    }

    fun resetUpdateNoteMessage(){
        updateNoteMsg = MutableLiveData<Resource<Note>>()
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun getSingleNote(noteId: Int) = viewModelScope.launch{
        singleNote = repository.getSingleNote(noteId)
    }

    fun updateNote(title: String,description: String,imageUrl: String,editedDate: String,noteId: Int) = CoroutineScope(
        Dispatchers.IO).launch  {
        repository.updateNote(title,description,imageUrl,editedDate,noteId)
    }



    fun saveNoteForInsert(title: String, description: String, createdDate: String, imageUrl: String){
        if(title.isEmpty() || description.isEmpty()){
            insertNoteMsg.postValue(Resource.error("Title and Description cannot be empty.",null))
            return
        }else{
            val note = Note(title, description, createdDate, imageUrl)
            insertNote(note)
            insertNoteMsg.postValue(Resource.success(note))
        }
    }

    fun saveNoteForUpdate(title: String, description: String, createdDate: String, editedDate: String, imageUrl: String, noteId: Int){
        if(title.isEmpty() || description.isEmpty()){
            updateNoteMsg.postValue(Resource.error("Title and Description cannot be empty.",null))
            return
        }else{
            val note = Note(title,description,createdDate,imageUrl,editedDate)
            updateNote(title,description,imageUrl,editedDate,noteId)
            updateNoteMsg.postValue(Resource.success(note))
        }
    }




}