package com.geektech.myapplication.ui.main

//noinspection SuspiciousImport
import android.R
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.geektech.myapplication.MainViewModel
import com.geektech.myapplication.data.model.Task
import com.geektech.myapplication.databinding.ActivityMainBinding
import com.geektech.myapplication.ui.task.TaskActivity
import com.geektech.myapplication.ui.task.TaskAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val adapter = TaskAdapter(
        this::onLongClickTask,
        this::isCheckedTask,
        this::onClickTask,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.recyclerView.adapter = adapter

        initView()
        initClick()
        initSpinner()
    }

    private fun initClick() {
        binding.btnTask.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        viewModel.list.observe(this) { updatedList ->
            binding.recyclerView.adapter = adapter

            adapter.setList(updatedList)
        }
    }

    private fun onLongClickTask(task: Task) { // deleteTaskClick
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(com.geektech.myapplication.R.string.хотите_удалить))
            .setMessage(getString(com.geektech.myapplication.R.string.данные_удалены))
            .setPositiveButton(getString(com.geektech.myapplication.R.string.да)) { dialog: DialogInterface, _: Int ->
                viewModel.deleteTask(task)
                adapter.delete(task)
                dialog.dismiss()
            }
            .setNegativeButton(getString(com.geektech.myapplication.R.string.отмена)) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        dialogBuilder.show()
    }

    private fun onClickTask(task: Task) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(UPDATE_TASK_KEY, task)
        startActivity(intent)
    }

    private fun isCheckedTask(task: Task, isChecked: Boolean = false) {
        viewModel.checkedTask(task, isChecked)
    }

    private fun initSpinner() {
        val taskFilterList = arrayOf(
            getString(com.geektech.myapplication.R.string.Все_задачи),
            getString(com.geektech.myapplication.R.string.Невыполненные),
            getString(com.geektech.myapplication.R.string.Выполненные)
        )

        val adapterSpinner = ArrayAdapter(this, R.layout.simple_spinner_item, taskFilterList)
        adapterSpinner.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapterSpinner

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (taskFilterList[position]) {
                    getString(com.geektech.myapplication.R.string.Все_задачи) -> viewModel.getTasks()
                    getString(com.geektech.myapplication.R.string.Невыполненные) -> viewModel.filterTasksFalse()
                    getString(com.geektech.myapplication.R.string.Выполненные) -> viewModel.filterTasksTrue()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.getTasks()
            }
        }
    }

    companion object {
        const val UPDATE_TASK_KEY = "update_task.key"
    }

    override fun onResume() {
        super.onResume()
        initSpinner()
    }
}