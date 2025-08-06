package com.example.hackathon.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathon.R
import com.example.hackathon.data.StudyManager
import com.example.hackathon.data.UserManager
import com.example.hackathon.ui.search.Study

class MyStudyAdapter(
    private val onStudyClick: (Study) -> Unit
) : ListAdapter<Study, MyStudyAdapter.MyStudyViewHolder>(StudyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStudyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_study, parent, false)
        return MyStudyViewHolder(view, onStudyClick)
    }

    override fun onBindViewHolder(holder: MyStudyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyStudyViewHolder(
        itemView: View,
        private val onStudyClick: (Study) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val textStudyName: TextView = itemView.findViewById(R.id.text_study_name)
        private val textStudyCategory: TextView = itemView.findViewById(R.id.text_study_category)
        private val textStudyCreator: TextView = itemView.findViewById(R.id.text_study_creator)
        private val textMemberCount: TextView = itemView.findViewById(R.id.text_member_count)
        
        fun bind(study: Study) {
            textStudyName.text = study.name
            textStudyCategory.text = study.category
            textStudyCreator.text = "스터디장: ${study.creator}"
            
            // 멤버 수 표시
            val studyManager = StudyManager(itemView.context)
            val memberCount = studyManager.getStudyMemberCount(study.id)
            textMemberCount.text = "멤버: ${memberCount}명"
            
            // 클릭 리스너 설정
            itemView.setOnClickListener {
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