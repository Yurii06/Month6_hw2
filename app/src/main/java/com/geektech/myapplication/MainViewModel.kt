package com.geektech.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geektech.myapplication.data.model.Task

class MainViewModel : ViewModel() {

    private val dao = App.db.taskDao()

    private var tasks = dao.getAll().toMutableList()
    private var _list = MutableLiveData(tasks)
    val list: LiveData<MutableList<Task>> = _list

    fun getTasks() {
        tasks = dao.getAll().toMutableList()
        _list.value = tasks
        Log.e("ololo", "getTask -> ${list.value}")
    }

    fun addTask(task: Task) {
        dao.insert(task)
    }

    fun updateTask(task: Task) {
        dao.update(task)
    }

    fun deleteTask(task: Task) {
        dao.delete(task)
    }

    fun checkedTask(task: Task, isChecked: Boolean) {

        val data = task.copy(
            checkBox = isChecked
        )

        Log.e("ololo","isCheck -> $data")

        dao.update(data)

    }

    fun filterTasksFalse() {
        tasks = dao.getTasksFalse().toMutableList()
        _list.value = tasks
        Log.e("ololo","isTaskFalse -> ${_list.value}")
    }

    fun filterTasksTrue() {
        tasks = dao.getTasksTrue().toMutableList()
        _list.value = tasks
        Log.e("ololo","isTaskTrue -> ${_list.value}")
    }
}