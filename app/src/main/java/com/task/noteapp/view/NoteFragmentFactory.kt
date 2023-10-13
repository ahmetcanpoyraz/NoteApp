package com.task.noteapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.task.noteapp.adapter.NoteRecylerAdapter
import javax.inject.Inject

// Created for using injection
class NoteFragmentFactory @Inject constructor(
    private val noteRecylerAdapter: NoteRecylerAdapter,
    private val glide : RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className){
            NoteFragment::class.java.name -> NoteFragment(noteRecylerAdapter)
            NoteDetailsFragment::class.java.name -> NoteDetailsFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }

}