package com.medina.agenda

import android.app.Application
import androidx.room.Room

class Agenda:Application() {
    companion object {
        lateinit var database: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Agenda.database = Room.databaseBuilder(this, TaskDatabase::class.java, "tasks-db").build()
    }
}