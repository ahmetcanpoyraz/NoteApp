package com.task.noteapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentNoteDetailBinding
import com.task.noteapp.util.Status
import com.task.noteapp.viewmodel.NoteDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@SuppressLint("SimpleDateFormat")
class NoteDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_note_detail) {

    lateinit var viewModel: NoteDetailsViewModel
    private var fragmentBinding : FragmentNoteDetailBinding? = null
    private val simpleDateFormat = SimpleDateFormat("dd/M/yyyy")
    val  args: NoteDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = simpleDateFormat.format(Date())
        viewModel = ViewModelProvider(requireActivity()).get(NoteDetailsViewModel::class.java)
        val binding = FragmentNoteDetailBinding.bind(view)
        fragmentBinding = binding
        val isClickedAddNote = args.isClickedForAddNote
        viewModel.resetUpdateNoteMessage()
        viewModel.resetInsertNoteMessage()

        // When ImageUrl change Glide upload photo
        binding.editTextImageUrl.addTextChangedListener{
            it?.let {
                if (it.toString().isNotEmpty()) {
                    glide.load(it.toString()).into(binding.detailImageView)
                }
            }
        }
        // If it is true fragments displays as add note fragments
        if(isClickedAddNote){
            subscribeToObservers()
            binding.saveNoteButton.setOnClickListener {
                viewModel.saveNoteForInsert(
                    binding.editTextTitle.text.toString(),
                    binding.editTextDescription.text.toString(),
                    currentDate,
                    binding.editTextImageUrl.text.toString())
            }
        }
        // If it is false fragments displays as detail note fragments
        else{
            viewModel.getSingleNote(args.noteId)
            binding.saveNoteButton.text = "Update Note"
            subscribeToSingleNoteObserver(binding)
            subscribeToUpdateNoteObserver()

            binding.saveNoteButton.setOnClickListener {
                viewModel.saveNoteForUpdate(
                    binding.editTextTitle.text.toString(),
                    binding.editTextDescription.text.toString(),
                    viewModel.singleNote.value?.createdDate!!,
                    currentDate,
                    binding.editTextImageUrl.text.toString(),
                    viewModel.singleNote.value?.id!!)
            }

        }

    }
    private fun subscribeToSingleNoteObserver(binding: FragmentNoteDetailBinding){
        viewModel.singleNote.observe(viewLifecycleOwner, Observer {
            binding.editTextTitle.text = it.title.toEditable()
            binding.editTextDescription.text = it.description.toEditable()
            binding.editTextImageUrl.text = it.imageUrl.toEditable()
            if (it.imageUrl.isNotEmpty()) {
                glide.load(it.imageUrl).into(binding.detailImageView)
            }
        })
    }


    private fun subscribeToObservers(){

        viewModel.insertNoteMessage.observe(viewLifecycleOwner, Observer{
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireActivity(),"Successfully Added",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
            }
        })
    }

    private fun subscribeToUpdateNoteObserver(){

        viewModel.updateNoteMessage.observe(viewLifecycleOwner, Observer{
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireActivity(),"Successfully Updated",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
            }
        })
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    override fun onDestroy() {
        fragmentBinding = null
        super.onDestroy()
    }
}