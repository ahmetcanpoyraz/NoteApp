package com.task.noteapp.viewmodel

import com.task.noteapp.repository.FakeNoteRepository
import org.junit.Before
import org.junit.Test

class NoteViewModelTest {

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup(){
        viewModel = NoteViewModel(FakeNoteRepository())
    }

}