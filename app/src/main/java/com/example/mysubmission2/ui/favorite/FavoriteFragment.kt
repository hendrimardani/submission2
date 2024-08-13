package com.example.mysubmission2.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission2.adapter.EventAdapter
import com.example.mysubmission2.databinding.FragmentFavoriteBinding
import com.example.mysubmission2.ui.EventViewModel
import com.example.mysubmission2.ui.ViewModelFactory

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
        getFavorite(viewModel, eventAdapter)

        if (isLinearLayout) {
            binding.rvFavorite.setPadding(0, 0, 0, 120)
        }

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = eventAdapter
        }
    }

    private fun getFavorite(viewModel: EventViewModel, eventAdapter: EventAdapter) {
        viewModel.getFavorite().observe(requireActivity()) { listEventEntity ->
            eventAdapter.submitList(listEventEntity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}