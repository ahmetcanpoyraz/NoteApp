package com.task.noteapp.roomdb


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.model.Note
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class NoteDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var dao : NoteDao

    @Inject
    @Named("testDatabase")
    lateinit var database: NoteDatabase

    @Before
    fun setup(){
      /*  database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),NoteDatabase::class.java
        ).allowMainThreadQueries().build()*/

        hiltRule.inject()

        dao = database.noteDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertNoteTesting() = runBlocking{
        val exampleNote = Note("title","desc","","25.03.2023","",1)
        dao.insertNote(exampleNote)

        val list = dao.observeNotes().getOrAwaitValue()
        assertThat(list).contains(exampleNote)
    }

    @Test
    fun deleteNoteTesting() = runBlocking{
        val exampleNote = Note("title","desc","","25.03.2023","",1)
        dao.deleteNote(exampleNote)

        val list = dao.observeNotes().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleNote)
    }
}