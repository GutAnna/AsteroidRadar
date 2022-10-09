package com.udacity.asteroidradar.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidViewItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidAdapter(val clickListener: AsteroidListener) : ListAdapter<Asteroid,
        AsteroidAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AsteroidViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieImg = getItem(position)
        holder.bind(movieImg, clickListener)
    }

    class ViewHolder(private val binding: AsteroidViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(Asteroid: Asteroid, clickListener: AsteroidListener) {
            binding.asteroid = Asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}