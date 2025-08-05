package com.example.hackathon.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathon.databinding.ItemStudyBinding

class StudyAdapter : ListAdapter<Study, StudyAdapter.StudyViewHolder>(StudyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyViewHolder {
        val binding = ItemStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StudyViewHolder(private val binding: ItemStudyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(study: Study) {
            binding.studyNameText.text = study.name
            binding.studyDescriptionText.text = study.description
        }
    }

    private class StudyDiffCallback : DiffUtil.ItemCallback<Study>() {
        override fun areItemsTheSame(oldItem: Study, newItem: Study): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Study, newItem: Study): Boolean {
            return oldItem == newItem
        }
    }
}