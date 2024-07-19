package com.medina.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

public class TaskAdapterActivity(
    val tasks: List<TaskEntity>,
    val checkTask: (TaskEntity) -> Unit,
    val deleteTask: (TaskEntity) -> Unit): RecyclerView.Adapter<TaskAdapterActivity.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            return ViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int){
            val item = tasks[position]
            holder.bind(item, checkTask, deleteTask)
        }

        override fun getItemCount(): Int{
            return tasks.size
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            private val tvTask = view.findViewById<TextView>(R.id.tvTask)
            private val cbIsDone = view.findViewById<CheckBox>(R.id.cbIsDone)

            fun bind(
                task: TaskEntity,
                checkTask: (TaskEntity) -> Unit,
                deleteTask: (TaskEntity) -> Unit
            ) {
                tvTask.text = task.name
                cbIsDone.isChecked = task.isDone
                cbIsDone.setOnClickListener{ checkTask(task)}
                itemView.setOnClickListener{ deleteTask(task)}
            }
        }
}