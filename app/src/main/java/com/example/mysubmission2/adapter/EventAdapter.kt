package com.example.mysubmission2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmission2.data.local.entity.EventEntity
import com.example.mysubmission2.databinding.ItemListBinding
import com.example.mysubmission2.adapter.EventAdapter.MyViewHolder

class EventAdapter : ListAdapter<EventEntity, MyViewHolder>(DIFF_CALLBACK){

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.tvTitleFinished.text = event.name
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.ivFinished)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
            object : DiffUtil.ItemCallback<EventEntity>() {
                override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}