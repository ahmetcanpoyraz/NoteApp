package com.task.noteapp.view

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.noteapp.R
import com.task.noteapp.adapter.NoteRecylerAdapter
import com.task.noteapp.databinding.FragmentNotesBinding
import com.task.noteapp.viewmodel.NoteDetailsViewModel
import com.task.noteapp.viewmodel.NoteViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

class NoteFragment @Inject constructor(
    val noteRecyclerAdapter : NoteRecylerAdapter
) : Fragment(R.layout.fragment_notes) {

    private var fragmentBinding : FragmentNotesBinding? = null
    lateinit var viewModel : NoteViewModel

    // Items when swipe to left shows delete icon and delete from db, list and recyclerview
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
          val layoutPosition = viewHolder.layoutPosition
          val selectedNote = noteRecyclerAdapter.notes[layoutPosition]
          viewModel.deleteNote(selectedNote)
        }
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
             RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addBackgroundColor(ContextCompat.getColor(requireContext(),R.color.red))
                .addActionIcon(R.drawable.ic_delete)
                .create()
                .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)
        val binding = FragmentNotesBinding.bind(view)
        fragmentBinding = binding
        subscribeToObservers()
        binding.recylerViewNotes.adapter = noteRecyclerAdapter
        binding.recylerViewNotes.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recylerViewNotes)


        binding.fab.setOnClickListener{
            findNavController().navigate(NoteFragmentDirections.actionNoteFragmentToNoteDetailsFragment(true))
        }
    }

    private fun subscribeToObservers(){
        viewModel.noteList.observe(viewLifecycleOwner, Observer {
            noteRecyclerAdapter.notes = it
        })
    }

    override fun onDestroy() {
        fragmentBinding = null
        super.onDestroy()
    }
}