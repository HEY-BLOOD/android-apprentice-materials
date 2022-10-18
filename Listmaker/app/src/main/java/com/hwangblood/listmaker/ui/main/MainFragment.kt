package com.hwangblood.listmaker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwangblood.listmaker.TaskList
import com.hwangblood.listmaker.databinding.FragmentMainBinding

class MainFragment() : Fragment(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    lateinit var clickListener: MainFragmentInteractionListener

    companion object {
        fun newInstance() = MainFragment()
    }

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(), MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(requireActivity())
            )
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.listsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists, this)
        binding.listsRecyclerview.adapter = recyclerViewAdapter
        viewModel.onListAdded = {
            recyclerViewAdapter.listsUpdated()
        }
        return binding.root
    }

    override fun listItemClicked(list: TaskList) {
        clickListener.listItemTapped(list)
    }
}