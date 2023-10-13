package com.task.noteapp.view

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.task.noteapp.R
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.launchFragmentInHiltContainer
import com.task.noteapp.repository.FakeNoteRepository
import com.task.noteapp.viewmodel.NoteDetailsViewModel
import com.task.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class NoteDetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory : NoteFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun testOnBackPressed() {
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<NoteDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(),navController)
        }

        pressBack()
        verify(navController).popBackStack()
    }

    @Test
    fun testSave() {
        val testViewModel = NoteDetailsViewModel(FakeNoteRepository())
        val testNoteViewModel = NoteViewModel(FakeNoteRepository())
        launchFragmentInHiltContainer<NoteDetailsFragment>(
            factory = fragmentFactory
        ) {
            viewModel = testViewModel
        }

        onView(withId(R.id.title)).perform(replaceText("title"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("desc"))
        onView(withId(R.id.editTextImageUrl)).perform(replaceText("urlll"))
        onView(withId(R.id.saveNoteButton)).perform(click())

        assertThat(testNoteViewModel.noteList.getOrAwaitValue()).contains(
            com.task.noteapp.model.Note(
                "title",
                "desc",
                "25.03.2023", "urlll"
            )
        )

    }

}
