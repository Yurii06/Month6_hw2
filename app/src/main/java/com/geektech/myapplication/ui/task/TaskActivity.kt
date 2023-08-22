package com.geektech.myapplication.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.ViewModelProvider
import com.geektech.myapplication.ui.main.MainActivity
import com.geektech.myapplication.MainViewModel
import com.geektech.myapplication.R
import com.geektech.myapplication.data.model.Task
import com.geektech.myapplication.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var viewModel: MainViewModel

    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        task = intent.getSerializableExtra(MainActivity.UPDATE_TASK_KEY) as Task?

        if (task == null){
            createTask()
        }else{
            initView()
        }
    }

    private fun createTask() {
        binding.btnSave.setOnClickListener {

            val data = Task(
                title = binding.etTitle.text.toString(),
                desc = binding.etDescription.text.toString(),
            )

            viewModel.addTask(data)

            finish()
        }
    }

    private fun initView() {
        with(binding) {
            etTitle.setText(task?.title)
            etDescription.setText(task?.desc)
            btnSave.text = "Update"

            btnSave.setOnClickListener {

                val data = task!!.copy(
                    title = binding.etTitle.text.toString(),
                    desc = binding.etDescription.text.toString(),
                )

                viewModel.updateTask(data)

                finish()
            }
        }
    }
}