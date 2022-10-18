package com.hwangblood.listmaker.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.hwangblood.listmaker.MainActivity
import com.hwangblood.listmaker.R
import com.hwangblood.listmaker.databinding.ActivityListDetailBinding

class ListDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDetailBinding

    lateinit var viewModel: ListDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListDetailFragment.newInstance()).commitNow()
        }

        viewModel = ViewModelProvider(this).get(ListDetailViewModel::class.java)

        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!

        title = viewModel.list.name

        binding.addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }
    }

    private fun showCreateTaskDialog() {
        //1
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this).setTitle(R.string.task_to_add).setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)

                dialog.dismiss()
            }.create().show()
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(
            MainActivity.INTENT_LIST_KEY, viewModel.list
        )
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }
}