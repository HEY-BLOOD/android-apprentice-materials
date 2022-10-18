package com.hwangblood.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.hwangblood.listmaker.databinding.ActivityMainBinding
import com.hwangblood.listmaker.ui.detail.ListDetailActivity
import com.hwangblood.listmaker.ui.detail.ListDetailFragment
import com.hwangblood.listmaker.ui.main.MainFragment
import com.hwangblood.listmaker.ui.main.MainViewModel
import com.hwangblood.listmaker.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        const val INTENT_LIST_KEY = "list"
    }

    // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                data?.let {
                    viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
                    viewModel.refreshLists()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(this)
            )
        ).get(MainViewModel::class.java)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this@MainActivity

            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container
            } else {
                R.id.main_fragment_container
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }
        }

        binding.listDetailFragmentContainer?.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.list_detail_background
            )
        )
        binding.floatingActionButton.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)
        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT
        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
        }
        builder.create().show()
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this).setTitle(R.string.task_to_add).setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }.create().show()
    }

    private fun showListDetail(list: TaskList) {
        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(
                this, ListDetailActivity::class.java
            )
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            resultLauncher.launch(listDetailIntent)
        } else {
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.list_detail_fragment_container,
                    ListDetailFragment::class.java,
                    bundle,
                    null
                )
            }
        }
        binding.floatingActionButton.setOnClickListener {
            showCreateTaskDialog()
        }

    }

    override fun onBackPressed() {
        val listDetailFragment = supportFragmentManager.findFragmentById(
            R.id.list_detail_fragment_container
        )
        if (listDetailFragment == null) {
            super.onBackPressed()
        } else {
            title = resources.getString(R.string.app_name)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }

            binding.floatingActionButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }
}