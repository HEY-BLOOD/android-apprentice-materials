package com.hwangblood.listmaker.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwangblood.listmaker.MainActivity
import com.hwangblood.listmaker.TaskList
import com.hwangblood.listmaker.databinding.FragmentListDetailBinding
import com.hwangblood.listmaker.ui.main.MainViewModel
import com.hwangblood.listmaker.ui.main.MainViewModelFactory

class ListDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ListDetailFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(), MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(requireActivity())
            )
        ).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: TaskList? = arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)
        if (list != null) {
            viewModel.list = list
            requireActivity().title = list.name
        }

        val recyclerAdapter = ListItemsRecyclerViewAdapter(viewModel.list)
        binding.listItemsRecyclerview.adapter = recyclerAdapter
        binding.listItemsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.onTaskAdded = {
            recyclerAdapter.notifyItemInserted(viewModel.list.tasks.size - 1)
        }
    }

}