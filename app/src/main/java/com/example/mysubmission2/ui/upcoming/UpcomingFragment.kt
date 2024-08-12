package com.example.mysubmission2.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.mysubmission2.R
import com.example.mysubmission2.databinding.FragmentUpcomingBinding
import com.example.mysubmission2.ui.EventViewModel
import com.example.mysubmission2.ui.ViewModelFactory
import com.example.mysubmission2.data.Result
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.ui.detail.DetailActivity.Companion.EXTRA_ACTIVITY
import com.example.mysubmission2.ui.detail.DetailActivity.Companion.EXTRA_ID

class UpcomingFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    var id: String? = null


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

        binding.cvUpcoming.setOnClickListener(this)
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
                            getOutputUpcoming(it)
                            Log.e(TAG, it.name as String)
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

    private fun getOutputUpcoming(item: EventEntity) {
        Glide.with(this@UpcomingFragment)
            .load(item.mediaCover)
            .into(binding.ivUpcoming)
        binding.tvTitleUpcoming.text = item.name
        id = item.id
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cv_upcoming -> {
                val bundle = Bundle()
                bundle.putString(EXTRA_ACTIVITY, UPCOMING_FRAGMENT)
                bundle.putString(EXTRA_ID, id)
                v.findNavController().navigate(R.id.action_navigation_upcoming_to_detailActivity, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "UpComingFragment TEST REST API"
        const val UPCOMING_FRAGMENT = "UpcomingFragment"
    }
}