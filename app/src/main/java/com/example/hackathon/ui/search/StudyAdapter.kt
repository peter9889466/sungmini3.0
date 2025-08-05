package com.example.hackathon.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathon.databinding.ItemStudyBinding

class StudyAdapter(private val onStudyClick: (Study) -> Unit) : ListAdapter<Study, StudyAdapter.StudyViewHolder>(StudyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyViewHolder {
        val binding = ItemStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudyViewHolder(binding, onStudyClick)
    }

    override fun onBindViewHolder(holder: StudyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StudyViewHolder(
        private val binding: ItemStudyBinding,
        private val onStudyClick: (Study) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(study: Study) {
            binding.studyNameText.text = study.name
            binding.studyDescriptionText.text = study.description
            binding.studyCreatorText.text = "스터디장: ${study.creator}"
            binding.studyCategoryText.text = study.category
            
            binding.root.setOnClickListener {
                onStudyClick(study)
            }
        }
    }

    private class StudyDiffCallback : DiffUtil.ItemCallback<Study>() {
        override fun areItemsTheSame(oldItem: Study, newItem: Study): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Study, newItem: Study): Boolean {
            return oldItem == newItem
        }
    }
}