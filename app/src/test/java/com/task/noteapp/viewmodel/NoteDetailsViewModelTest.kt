package com.task.noteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.MainCoroutineRule
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.repository.FakeNoteRepository
import com.task.noteapp.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteDetailsViewModelTest {

    // Added For work on main thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: NoteDetailsViewModel

    @Before
    fun setup(){
        viewModel = NoteDetailsViewModel(FakeNoteRepository())
    }

    @Test
    fun `insert note without title returns error` (){
        viewModel.saveNoteForInsert("","description","25.03.2023","")

        // Convert live data to normal data
        val value = viewModel.insertNoteMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert note without description returns error` (){
        viewModel.saveNoteForInsert("asdasd","","25.03.2023","")

        // Convert live data to normal data
        val value = viewModel.insertNoteMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}