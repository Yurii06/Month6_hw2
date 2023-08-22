package com.geektech.myapplication

import android.app.Application
import androidx.room.Room
import com.geektech.myapplication.data.db.AppDatabase

class App : Application(){

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Task-file"
        ).allowMainThreadQueries().build()
    }

}