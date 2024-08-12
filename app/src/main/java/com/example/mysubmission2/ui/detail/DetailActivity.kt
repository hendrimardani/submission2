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
import com.bumptech.glide.Glide
import com.example.mysubmission2.R
import com.example.mysubmission2.adapter.EventAdapter.Companion.EVENT_ADAPTER
import com.example.mysubmission2.data.Result
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.data.remote.response.Detail
import com.example.mysubmission2.databinding.ActivityDetailBinding
import com.example.mysubmission2.ui.EventViewModel
import com.example.mysubmission2.ui.ViewModelFactory
import com.example.mysubmission2.ui.upcoming.UpcomingFragment.Companion.UPCOMING_FRAGMENT

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    var isFavorite = false

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

        val id = intent.getStringExtra(EXTRA_ID).toString()
        val activity = intent.getStringExtra(EXTRA_ACTIVITY)

        if (activity == EVENT_ADAPTER) {
            Log.e(TAG_FINISHED, id)
            getFinished(viewModel, id)
            getIsFavorite(viewModel, id)
            viewModel.isEventFavorite(id).observe(this) { isFavorite ->
                setFiiledFavorite(isFavorite)
            }
        } else if (activity == UPCOMING_FRAGMENT) {
            Log.e(TAG_UPCOMING, id)
            getUpComing(viewModel)
            getIsFavorite(viewModel, id)
            viewModel.isEventFavorite(id).observe(this) { isFavorite ->
                setFiiledFavorite(isFavorite)
            }
        }
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
                            getOutputUpcoming(it)
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

    private fun getFinished(viewModel: EventViewModel, id: String) {
        viewModel.getFinished().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        viewModel.getDetail(id)
                        viewModel.detail.observe(this) {
                            getOutputFinished(it)
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

    private fun getIsFavorite(viewModel: EventViewModel, id: String) {
        binding.fabDetail.setOnClickListener {
            isFavorite = !isFavorite
            viewModel.updateFavoriteEvent(id, isFavorite)
        }
    }

    private fun setFiiledFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabDetail.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite_filled))
        } else {
            binding.fabDetail.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite))
        }
    }

    private fun getOutputUpcoming(item: EventEntity) {
        Glide.with(this@DetailActivity)
            .load(item.mediaCover)
            .into(binding.ivDetail)
        binding.tvTitleDetail.text = item.name
        binding.tvSummaryDetail.text = item.summary
        binding.tvDescription.text = item.description
    }

    private fun getOutputFinished(item: Detail) {
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
        private const val TAG_UPCOMING = "DetailActivity UPCOMING TEST KLIK"
        private const val TAG_FINISHED = "DetailActivity FINISHED TEST KLIK"

        const val EXTRA_ID = "extra_id"
        const val EXTRA_ACTIVITY = "extra_activity"
    }
}