package com.task.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    var title : String,
    var description : String,
    var createdDate: String,
    var imageUrl: String,
    var editedDate: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)