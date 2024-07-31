package com.example.mysubmission2.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mysubmission2.R
import com.example.mysubmission2.data.Result
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.databinding.ActivityDetailBinding
import com.example.mysubmission2.ui.EventViewModel
import com.example.mysubmission2.ui.ViewModelFactory
import com.example.mysubmission2.ui.upcoming.UpcomingFragment

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventList: ArrayList<EventEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: EventViewModel by viewModels { factory }

        viewModel.getBookmarkedEvent().observe(this) {
            binding.progressBar.visibility = View.GONE
        }

        getUpComing(viewModel)
    }

    private fun getUpComing(viewModel: EventViewModel) {
        viewModel.getUpComing().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val eventData = result.data
                        eventData.forEach {
                            getOutputUpComing(it)

                            getIsBookmarkedClicked(it, viewModel)
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun getIsBookmarkedClicked(item: EventEntity, viewModel: EventViewModel) {
        binding.fabDetail.setOnClickListener {
            if (item.isBookmarked) {
                viewModel.deleteEvent(item)
                binding.fabDetail.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite_filled))
                Log.e(TAG, "Terklik")
            } else {
                viewModel.saveEvent(item)
                binding.fabDetail.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite))
                Log.e(TAG, "Tidak Terklik")
            }
        }
    }

    private fun getOutputUpComing(item: EventEntity) {
        Glide.with(this@DetailActivity)
            .load(item.mediaCover)
            .into(binding.ivDetail)
        binding.tvTitleDetail.text = item.name
        binding.tvSummaryDetail.text = item.summary
        binding.tvDescription.text = item.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "DETAIL ACTIVITY TEST KLIK"
        const val EXTRA_NAME = "extra_name"
    }
}