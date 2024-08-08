package com.example.mysubmission2.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission2.adapter.EventAdapter
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.databinding.FragmentFavoriteBinding
import com.example.mysubmission2.ui.EventViewModel
import com.example.mysubmission2.ui.ViewModelFactory
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels { factory }
        val isLinearLayout = true

        val eventAdapter = EventAdapter()
        getLiveDataFavorite(viewModel, eventAdapter)

        if (isLinearLayout) binding.rvFavorite.setPadding(0, 0, 0, 120)

        getListFavorite(viewModel, eventAdapter)


//        if (itemList.isNotEmpty()) {
//            isTexViewVisible(false)
//            binding.rvFavorite.apply {
//                layoutManager = LinearLayoutManager(requireActivity())
//                setHasFixedSize(true)
//                adapter = eventAdapter
//            }
//        } else isTexViewVisible(true)

    }

    private fun isTexViewVisible(isVisible: Boolean) {
        if (isVisible) binding.tvBelumAdaItemFavorite.visibility = View.VISIBLE
        else binding.tvBelumAdaItemFavorite.visibility = View.GONE
    }

    private fun getListFavorite(viewModel: EventViewModel, eventAdapter: EventAdapter) {
        val itemList = ArrayList<EventEntity>()
        lifecycleScope.launch {
            viewModel.getListFavorite().forEach {
                itemList.add(it)
            }
            if (itemList.isNotEmpty()) {
                isTexViewVisible(false)
                binding.rvFavorite.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    setHasFixedSize(true)
                    adapter = eventAdapter
                }
            } else isTexViewVisible(true)
        }
    }

    private fun getLiveDataFavorite(viewModel: EventViewModel, eventAdapter: EventAdapter) {
        viewModel.getFavorite().observe(requireActivity()) { listEventEntity ->
            eventAdapter.submitList(listEventEntity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavoriteFragment TEST TEST"
    }
}