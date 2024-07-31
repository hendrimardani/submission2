package com.example.mysubmission2.ui.upcoming

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mysubmission2.databinding.FragmentUpcomingBinding
import com.example.mysubmission2.ui.EventViewModel
import com.example.mysubmission2.ui.ViewModelFactory
import com.example.mysubmission2.data.Result

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels { factory }

        getUpComing(viewModel)
    }

    private fun getUpComing(viewModel: EventViewModel) {
        viewModel.getUpComing().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val eventData = result.data
                        eventData.forEach {
                            Glide.with(this@UpcomingFragment)
                                .load(it.mediaCover)
                                .into(binding.ivUpcoming)
                            binding.tvTitleUpcoming.text = it.name
                            Log.e(TAG, it.name.toString())
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "UpComingActivity TEST REST API"
    }
}