package com.task.noteapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.RequestManager
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentNotesBinding
import com.task.noteapp.model.Note
import com.task.noteapp.view.NoteFragmentDirections
import com.task.noteapp.viewmodel.NoteDetailsViewModel
import javax.inject.Inject

class NoteRecylerAdapter @Inject constructor(
    val glide: RequestManager
): RecyclerView.Adapter<NoteRecylerAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // It is a class that calculate difference between two list and update necessary items
    private val diffUtil = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
    // If any changes update list
    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)
    var notes: List<Note>
    get() = recyclerListDiffer.currentList
    set(value) = recyclerListDiffer.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_row,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.noteRowImageView)
        val titleText = holder.itemView.findViewById<TextView>(R.id.noteRowTitleText)
        val descriptionText = holder.itemView.findViewById<TextView>(R.id.noteRowDescriptionText)
        val createdDateText = holder.itemView.findViewById<TextView>(R.id.noteRowCreatedTimeText)
        val noteRowEditedText = holder.itemView.findViewById<TextView>(R.id.noteRowEditedText)
        val note = notes[position]
        holder.itemView.apply {
            titleText.text = "Title: ${note.title}"
            descriptionText.text = "Description: ${note.description}"
            createdDateText.text = "Created Date: ${note.createdDate}"
            if (note.imageUrl.isNotEmpty()) {
                glide.load(note.imageUrl).into(imageView)
            }
            if(note.editedDate?.isNotEmpty() == true){
                noteRowEditedText.visibility = View.VISIBLE
            }
        }
        // Go detail page when click item on recyclerview
        holder.itemView.setOnClickListener {
            val action = NoteFragmentDirections.actionNoteFragmentToNoteDetailsFragment(false,note.id!!)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

}