package com.medina.agenda

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var tasks: MutableList<TaskEntity>
    lateinit var btnAddTask: Button
    lateinit var etTask: EditText
    lateinit var miAdapter:TaskAdapterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnAddTask = findViewById(R.id.btnAddTask)
        etTask = findViewById(R.id.etTask)
        recyclerView = findViewById(R.id.rvTask)

        tasks = ArrayList()
        getTasks()

        btnAddTask.setOnClickListener{
            addTask(TaskEntity(name = etTask.text.toString()))
        }
    }

    fun clearFocus(){
        etTask.setText("")
    }

    fun Context.hideKeyboard(){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = (this as? Activity)?.currentFocus ?: View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun addTask(taskEntity: TaskEntity){
        CoroutineScope(Dispatchers.IO).launch {
            val id = Agenda.database.taskDao().addTask(taskEntity)
            val recoveryTask = Agenda.database.taskDao().getTaskById(id)

            withContext(Dispatchers.Main) {
                tasks.add(recoveryTask)
                miAdapter.notifyDataSetChanged()
                clearFocus()
                hideKeyboard()
            }
        }
    }

    fun getTasks(){
        CoroutineScope(Dispatchers.IO).launch {
            tasks = Agenda.database.taskDao().getAllTasks()

            withContext(Dispatchers.Main) {
                tasks.sortBy { it.id }
                setUpRecyclerView(tasks)
            }
        }
    }

    fun updateTask(task: TaskEntity){
        task.isDone = !task.isDone
        CoroutineScope(Dispatchers.IO).launch {
            Agenda.database.taskDao().updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity){
        val position = tasks.indexOf(task)
        CoroutineScope(Dispatchers.IO).launch {
            Agenda.database.taskDao().deleteTask(task)

            withContext(Dispatchers.Main) {
                tasks.remove(task)
                Toast.makeText(this@MainActivity, "delete ${tasks[position].name}", Toast.LENGTH_SHORT).show()
                miAdapter.notifyItemRemoved(position)
            }
        }
    }

    fun setUpRecyclerView(task: List<TaskEntity>){
        miAdapter = TaskAdapterActivity(tasks, {updateTask(it)}, {deleteTask(it)})
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = miAdapter
    }
}