package com.medina.agenda

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "task_entity")

data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name:String = " ",
    var isDone:Boolean = false
)
